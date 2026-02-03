package com.system.batch.lesson.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.listener.ExecutionContextPromotionListener;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.PlatformTransactionManager;

@Slf4j
public class SystemTerminationConfig {
    @Bean
    public Job systemTerminationJob(
            JobRepository jobRepository,
            Step scanningStep,
            Step eliminationStep
    ) {
        return new JobBuilder("systemTerminationJob", jobRepository)
                .start(scanningStep)
                .next(eliminationStep)
                .build();
    }

    @Bean
    public Step scanningStep(
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager
    ) {
        return new StepBuilder("scanningStep", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    String target = "판교 서버실";
                    ExecutionContext stepContext = contribution.getStepExecution().getExecutionContext(); //StepExecution의 ExecutionContext 가져오기
                    stepContext.put("targetSystem", target); //StepExecution의 ExecutionContext에 데이터 저장 (ExecutionContextPromotionListener 에 의해, JobExecutionContext 로 승격됨)
                    log.info("타겟 스캔 완료: {}", target);
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .listener(promotionListener()) //ExecutionContextPromotionListener 등록
                .build();
    }

    @Bean
    public Step eliminationStep(
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager,
            Tasklet eliminationTasklet
    ) {
        return new StepBuilder("eliminationStep", jobRepository)
                .tasklet(eliminationTasklet, transactionManager)
                .build();
    }

    @Bean
    @StepScope
    public Tasklet eliminationTasklet(
            @Value("#{jobExecutionContext['targetSystem']}") String target // JobExecutionContext 에서 데이터 조회 (ExecutionContextPromotionListener 에 의해 승격된 데이터)
    ) {
        return (contribution, chunkContext) -> {
            log.info("시스템 제거 작업 실행: {}", target);
            return RepeatStatus.FINISHED;
        };
    }

    @Bean
    public ExecutionContextPromotionListener promotionListener() {
        ExecutionContextPromotionListener listener = new ExecutionContextPromotionListener(); //리스너 객체 생성
        listener.setKeys(new String[]{"targetSystem"}); //Step 수준의 ExecutionContext에서 Job 수준으로 승격할 데이터의 키 값을 지정
        return listener;
    }
}
