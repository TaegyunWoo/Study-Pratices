package com.system.batch.lesson.rdbms;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class RdbmsHelloWorldJobConfig {
    @Bean
    public Job rdbmsHelloWorldJob(
        JobRepository jobRepository,
        Step rdbmsHelloWorldStep
    ) {
        return new JobBuilder("rdbmsHelloWorldJob", jobRepository)
            .start(rdbmsHelloWorldStep)
            .build();
    }

    @Bean
    public Step rdbmsHelloWorldStep(
        JobRepository jobRepository,
        PlatformTransactionManager platformTransactionManager
    ) {
        return new StepBuilder("rdbmsHelloWorldStep", jobRepository)
            .tasklet(rdbmsHelloWorldTasklet(), platformTransactionManager)
            .build();
    }

    public Tasklet rdbmsHelloWorldTasklet() {
        return (contribution, chunkContext) -> {
            System.out.println("Hello World!");
            return RepeatStatus.FINISHED;
        };
    }
}
