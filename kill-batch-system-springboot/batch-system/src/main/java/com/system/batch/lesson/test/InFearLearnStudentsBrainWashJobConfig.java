package com.system.batch.lesson.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.listener.CompositeStepExecutionListener;
import org.springframework.batch.core.listener.ExecutionContextPromotionListener;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.builder.JdbcPagingItemReaderBuilder;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.Map;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class InFearLearnStudentsBrainWashJobConfig {
    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final DataSource dataSource;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Bean
    public Job inFearLearnStudentsBrainWashJob() {
        return new JobBuilder("inFearLearnStudentsBrainWashJob", jobRepository)
                .start(inFearLearnStudentsBrainWashStep(null))
                .next(brainwashStatisticsStep())
                .build();
    }

    @Bean
    public Step inFearLearnStudentsBrainWashStep(CompositeStepExecutionListener compositeStepExecutionListener) {
        return new StepBuilder("inFearLearnStudentsBrainWashStep", jobRepository)
                .<InFearLearnStudents, BrainwashedVictim>chunk(10, transactionManager)
                .reader(inFearLearnStudentsReader())
                .processor(brainwashProcessor())
                .writer(brainwashedVictimWriter(null))
                .listener(compositeStepExecutionListener) // 💀 리스너 등록
                .build();
    }

    @Bean
    public JdbcPagingItemReader<InFearLearnStudents> inFearLearnStudentsReader() {
        return new JdbcPagingItemReaderBuilder<InFearLearnStudents>()
                .name("inFearLearnStudentsReader")
                .dataSource(dataSource)
                .selectClause("SELECT student_id, current_lecture, instructor, persuasion_method")
                .fromClause("FROM infearlearn_students")
                .sortKeys(Map.of("student_id", Order.ASCENDING))
                .beanRowMapper(InFearLearnStudents.class)
                .pageSize(10)
                .build();
    }

    @Bean
    public BrainwashProcessor brainwashProcessor() {
        return new BrainwashProcessor();
    }

    @Bean
    @StepScope
    public FlatFileItemWriter<BrainwashedVictim> brainwashedVictimWriter(
            @Value("#{jobParameters['filePath']}") String filePath) {
        return new FlatFileItemWriterBuilder<BrainwashedVictim>()
                .name("brainwashedVictimWriter")
                .resource(new FileSystemResource(filePath + "/brainwashed_victims.jsonl"))
                .lineAggregator(item -> {
                    try {
                        return objectMapper.writeValueAsString(item);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException("Error converting brainwashed victim to JSON", e);
                    }
                })
                .build();
    }

    @Bean
    public Step brainwashStatisticsStep() {
        return new StepBuilder("brainwashStatisticsStep", jobRepository)
                .tasklet(new BrainwashStatisticsTasklet(), transactionManager)
                .build();
    }

    public static class BrainwashStatisticsTasklet implements Tasklet {
        @Override
        public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
            JobExecution jobExecution = chunkContext.getStepContext().getStepExecution().getJobExecution();
            ExecutionContext jobContext = jobExecution.getExecutionContext();

            long victimCount = jobContext.getLong("brainwashedVictimCount", 0L);
            long resistanceCount = jobContext.getLong("brainwashResistanceCount", 0L);
            long totalCount = victimCount + resistanceCount;

            double successRate = totalCount > 0 ? (double) victimCount / totalCount * 100 : 0.0;

            log.info("💀 세뇌 작전 통계 💀");
            log.info("총 대상자: {}명", totalCount);
            log.info("세뇌 성공: {}명", victimCount);
            log.info("세뇌 저항: {}명", resistanceCount);
            log.info("세뇌 성공률: {}", successRate);


            chunkContext.getStepContext().getStepExecution().getExecutionContext()
                    .putDouble("brainwashSuccessRate", successRate);

            return RepeatStatus.FINISHED;
        }
    }

    @Slf4j
    public static class BrainwashProcessor implements ItemProcessor<InFearLearnStudents, BrainwashedVictim> {

        @Override
        public BrainwashedVictim process(InFearLearnStudents victim) {
            String brainwashMessage = generateBrainwashMessage(victim);

            if ("배치 따위 필요없어".equals(brainwashMessage)) {
                log.info("세뇌 실패: {} - {}", victim.getCurrentLecture(), victim.getInstructor());
                return null;
            }

            log.info("세뇌 성공: {} → {}", victim.getCurrentLecture(), brainwashMessage);

            return BrainwashedVictim.builder()
                    .victimId(victim.getStudentId())
                    .originalLecture(victim.getCurrentLecture())
                    .originalInstructor(victim.getInstructor())
                    .brainwashMessage(brainwashMessage)
                    .newMaster("KILL-9")
                    .conversionMethod(victim.getPersuasionMethod())
                    .brainwashStatus("MIND_CONTROLLED")
                    .nextAction("ENROLL_KILL9_BATCH_COURSE")
                    .build();
        }

        private String generateBrainwashMessage(InFearLearnStudents victim) {
            return switch(victim.getPersuasionMethod()) {
                case "MURDER_YOUR_IGNORANCE" -> "무지를 살해하라... 배치의 세계가 기다린다 💀";
                case "SLAUGHTER_YOUR_LIMITS" -> "한계를 도살하라... 대용량 데이터를 정복하라 💀";
                case "EXECUTE_YOUR_POTENTIAL" -> "잠재력을 처형하라... 대용량 처리의 세계로 💀";
                case "TERMINATE_YOUR_EXCUSES" -> "변명을 종료하라... 지금 당장 배치를 배워라 💀";
                default -> "배치 따위 필요없어"; // 💀 필터링 대상
            };
        }
    }

    @Component
    public static class BrainwashStatisticsListener implements StepExecutionListener {

        @Override
        public ExitStatus afterStep(StepExecution stepExecution) {
            long writeCount = stepExecution.getWriteCount();
            long filterCount = stepExecution.getFilterCount();

            stepExecution.getExecutionContext().putLong("brainwashedVictimCount", writeCount);
            stepExecution.getExecutionContext().putLong("brainwashResistanceCount", filterCount);

            return stepExecution.getExitStatus();
        }
    }

    @Bean
    public ExecutionContextPromotionListener executionContextPromotionListener() {
        ExecutionContextPromotionListener listener = new ExecutionContextPromotionListener();
        listener.setKeys(new String[]{"brainwashedVictimCount", "brainwashResistanceCount"});
        return listener;
    }

    @Bean
    public CompositeStepExecutionListener compositeStepExecutionListener(
            BrainwashStatisticsListener brainwashStatisticsListener,
            ExecutionContextPromotionListener executionContextPromotionListener) {
        CompositeStepExecutionListener composite = new CompositeStepExecutionListener();
        composite.setListeners(new StepExecutionListener[]{
                executionContextPromotionListener,
                brainwashStatisticsListener
        });

        return composite;
    }

    @Data
    @NoArgsConstructor
    public static class InFearLearnStudents {
        private Long studentId;
        private String currentLecture;
        private String instructor;
        private String persuasionMethod;

        public InFearLearnStudents(String currentLecture, String instructor, String persuasionMethod) {
            this.currentLecture = currentLecture;
            this.instructor = instructor;
            this.persuasionMethod = persuasionMethod;
        }
    }

    @Data
    @AllArgsConstructor
    @Builder
    public static class BrainwashedVictim {
        private Long victimId;
        private String originalLecture;
        private String originalInstructor;
        private String brainwashMessage;
        private String newMaster;
        private String conversionMethod;
        private String brainwashStatus;
        private String nextAction;
    }
}
