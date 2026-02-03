package com.system.batch.mvc.config;

import org.springframework.boot.autoconfigure.batch.BatchTaskExecutor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;

@Configuration
public class BatchCustomConfiguration {
    @Bean
    @BatchTaskExecutor
    public TaskExecutor taskExecutor() {
        return new SimpleAsyncTaskExecutor(); //비동기 배치 실행을 위해 TaskExecutor 등록
    }
}
