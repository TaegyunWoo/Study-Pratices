package com.system.batch.lesson.mandate;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.ClassifierCompositeItemWriter;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.classify.Classifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class MandateSystemLogProcessingConfig {
    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;

    @Bean
    public Job systemLogProcessingMandateJob(
        Step systemLogProcessingMandateStep
    ) {
        return new JobBuilder("systemLogProcessingMandateJob", jobRepository)
            .start(systemLogProcessingMandateStep)
            .build();
    }

    @Bean
    public Step systemLogProcessingMandateStep(
        ListItemReader<MandateSystemLog> systemLogListItemMandateReader,
        ClassifierCompositeItemWriter<MandateSystemLog> classifierWriter
    ) {
        return new StepBuilder("systemLogProcessingMandateStep", jobRepository)
            .<MandateSystemLog, MandateSystemLog>chunk(10, transactionManager)
            .reader(systemLogListItemMandateReader)
            .writer(classifierWriter)
            .build();
    }

    @Bean
    public ListItemReader<MandateSystemLog> systemLogListItemMandateReader() {
        List<MandateSystemLog> logs = new ArrayList<>();

        // 테스트용 데이터 생성
        MandateSystemLog criticalLog = new MandateSystemLog();
        criticalLog.setType("CRITICAL");
        criticalLog.setMessage("OOM 발생!! 메모리가 바닥났다!");
        criticalLog.setCpuUsage(95);
        criticalLog.setMemoryUsage(2024 * 1024 * 1024L);
        logs.add(criticalLog);

        MandateSystemLog normalLog = new MandateSystemLog();
        normalLog.setType("NORMAL");
        normalLog.setMessage("시스템 정상 작동 중");
        normalLog.setCpuUsage(30);
        normalLog.setMemoryUsage(512 * 1024 * 1024L);
        logs.add(normalLog);

        return new ListItemReader<>(logs);
    }

    /**
     * Classifier 에 의해 적절한 ItemWriter 가 선택되어 데이터 쓰기를 위임하는 ItemWriter
     */
    @Bean
    public ClassifierCompositeItemWriter<MandateSystemLog> classifierCompositeItemMandateWriter(
        ItemWriter<MandateSystemLog> criticalLogMandateWriter,
        ItemWriter<MandateSystemLog> normalLogMandateWriter
    ) {
        ClassifierCompositeItemWriter<MandateSystemLog> writer = new ClassifierCompositeItemWriter<>();
        writer.setClassifier(new MandateSystemLogClassifier(criticalLogMandateWriter, normalLogMandateWriter));
        return writer;
    }

    /**
     * 실제 데이터 쓰기를 담당하는 ItemWriter A
     */
    @Bean
    public ItemWriter<MandateSystemLog> normalLogMandateWriter() {
        return items -> {
            log.info("✅NoramLogWriter: 일반 로그 처리 중... 대충 파일에 출력하거나 하자..");
            for (MandateSystemLog item : items) {
                log.info("✅일반 처리: {}", item);
            }
        };
    }

    /**
     * 실제 데이터 쓰기를 담당하는 ItemWriter B
     */
    @Bean
    public ItemWriter<MandateSystemLog> criticalLogMandateWriter() {
        return items -> {
            log.info("🚨CriticalLogWriter: 치명적 시스템 로그 감지! 즉시 처리 시작!");
            for (MandateSystemLog item : items) {
                // 실제 운영에선 여기서 슬랙 혹은 이메일 발송
                log.info("🚨긴급 처리: {}", item);
            }
        };
    }

    /**
     * ItemWriter 분류
     */
    @RequiredArgsConstructor
    public static class MandateSystemLogClassifier implements Classifier<MandateSystemLog, ItemWriter<? super MandateSystemLog>> {
        public static final int CRITICAL_CPU_THRESHOLD = 90;
        public static final long CRITICAL_MEMORY_THRESHOLD = 1024 * 1024 * 1024; // 1GB

        private final ItemWriter<MandateSystemLog> criticalWriter;
        private final ItemWriter<MandateSystemLog> normalWriter;

        /**
         * 분류 로직
         */
        @Override
        public ItemWriter<? super MandateSystemLog> classify(MandateSystemLog mandateSystemLog) {
            if (isCritical(mandateSystemLog)) {
                return criticalWriter;
            } else {
                return normalWriter;
            }
        }

        // 시스템의 생사를 가르는 판단 기준
        private boolean isCritical(MandateSystemLog log) {
            return "CRITICAL".equals(log.getType()) ||
                log.getCpuUsage() >= CRITICAL_CPU_THRESHOLD ||
                log.getMemoryUsage() >= CRITICAL_MEMORY_THRESHOLD;
        }
    }

    @Data
    public static class MandateSystemLog {
        private String type;      // CRITICAL or NORMAL
        private String message;
        private int cpuUsage;
        private long memoryUsage;
    }
}
