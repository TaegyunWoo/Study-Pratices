package com.system.batch.lesson.flatfileitemreader;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.transform.RegexLineTokenizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.PlatformTransactionManager;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class RegexSystemLogJobConfig {
    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;

    @Bean
    public Job logAnalysisJob(Step logAnalysisStep) {
        return new JobBuilder("logAnalysisJob", jobRepository)
                .start(logAnalysisStep)
                .build();
    }

    @Bean
    public Step logAnalysisStep(
            FlatFileItemReader<LogEntry> logItemReader,
            ItemWriter<LogEntry> logItemWriter
    ) {
        return new StepBuilder("logAnalysisStep", jobRepository)
                .<LogEntry, LogEntry>chunk(10, transactionManager)
                .reader(logItemReader)
                .writer(logItemWriter)
                .build();
    }

    /**
     * 정규식 기반의 시스템 로그 파일을 읽기 위한 FlatFileItemReader 빈 정의
     */
    @Bean
    @StepScope
    public FlatFileItemReader<LogEntry> logItemReader(
            @Value("#jobParameters['inputFile']}") String inputFile
    ) {
        RegexLineTokenizer tokenizer = new RegexLineTokenizer();
        tokenizer.setRegex("\\[\\w+\\]\\[Thread-(\\d+)\\]\\[CPU: \\d+%\\] (.+)"); // 정규식 패턴 설정

        return new FlatFileItemReaderBuilder<LogEntry>()
                .name("logItemReader")
                .resource(new FileSystemResource(inputFile))
                .lineTokenizer(tokenizer)
                .fieldSetMapper( //토큰화된 첫번째 필드와 두번째 필드를 LogEntry 객체로 매핑
                        fieldSet -> new LogEntry(fieldSet.readString(0), fieldSet.readString(1))
                ).build();
    }

    @Bean
    public ItemWriter<LogEntry> logItemWriter() {
        return items -> {
            for (LogEntry logEntry : items) {
                log.info(
                        String.format("THD-%s: %s", logEntry.getThreadNum(), logEntry.getMessage())
                );
            }
        };
    }

    @Data
    @AllArgsConstructor
    public static class LogEntry {
        private String threadNum;
        private String message;
    }
}
