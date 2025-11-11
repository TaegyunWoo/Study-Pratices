package com.system.batch.config;

import com.system.batch.tasklet.ZombieProcessCleanupTasklet;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.support.transaction.ResourcelessTransactionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class ZombieBatchConfig {
    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;

    public ZombieBatchConfig(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        this.jobRepository = jobRepository;
        this.transactionManager = transactionManager;
    }

    @Bean
    public Tasklet zombieProcessCleanupTasklet() {
        return new ZombieProcessCleanupTasklet();
    }

    /**
     * StepBuilder.tasklet() 을 사용하여, 태스크릿 지향 처리 방식의 Step을 생성한다.
     */
    @Bean
    public Step zombieProcessCleanupStep() {
        return new StepBuilder("zombieCleanupStep", jobRepository)
//                .tasklet(zombieProcessCleanupTasklet(), transactionManager) // tasklet과 transaction manager 설정 1
                .tasklet(zombieProcessCleanupTasklet(), new ResourcelessTransactionManager()) // tasklet과 transaction manager 설정 2 (DB 트랜잭션 처리가 필요한 tasklet 이 아니므로, ResourcelessTransactionManager 사용)
                .build();
    }

    @Bean
    public Job zombieCleanupJob() {
        return new JobBuilder("zombieCleanupJob", jobRepository)
                .start(zombieProcessCleanupStep()) // 첫 번째(그리고 유일한) step 설정
                .build();
    }
}
