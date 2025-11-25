package com.system.batch.lesson.flatfileitemreader;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.transform.Range;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.PlatformTransactionManager;

import java.beans.PropertyEditor;
import java.beans.PropertyEditorSupport;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
//@Configuration
public class FixedLengthSystemFailureJobConfig {
    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;

    @Bean
    public Job fixedSystemFailureJob(Step fixedSystemFailureStep) {
        return new JobBuilder("fixedSystemFailureJob", jobRepository)
                .start(fixedSystemFailureStep)
                .build();
    }

    @Bean
    public Step fixedSystemFailureStep(
            FlatFileItemReader<FixedSystemFailure> systemFailureItemReader,
            SystemFailureStdoutItemWriter systemFailureStdoutItemWriter
    ) {
        return new StepBuilder("fixedSystemFailureStep", jobRepository)
                .<FixedSystemFailure, FixedSystemFailure>chunk(10, transactionManager)
                .reader(systemFailureItemReader)
                .writer(systemFailureStdoutItemWriter)
                .build();
    }

    /**
     * 고정 길이 형식의 시스템 장애 로그 파일을 읽기 위한 FlatFileItemReader 빈 정의
     */
    @Bean
    @StepScope
    public FlatFileItemReader<FixedSystemFailure> fixedSystemFailureItemReader(
            @Value("#{jobParameters['inputFile']}") String inputFile) {
        return new FlatFileItemReaderBuilder<FixedSystemFailure>()
                .name("fixedSystemFailureItemReader")
                .resource(new FileSystemResource(inputFile))
                .fixedLength() //고정 길이 형식임을 알림
                .columns(new Range[]{
                        new Range(1, 8),     // errorId: ERR001 + 공백 2칸
                        new Range(9, 29),    // errorDateTime: 날짜시간 + 공백 2칸
                        new Range(30, 39),   // severity: CRITICAL/FATAL + 패딩
                        new Range(40, 45),   // processId: 1234 + 공백 2칸
                        new Range(46, 66)    // errorMessage: 메시지 + \n
                }).names("errorId",
                        "errorDateTime",
                        "severity",
                        "processId",
                        "errorMessage")
                .targetType(FixedSystemFailure.class)
                .customEditors(Map.of(LocalDateTime.class, dateTimeEditor())) //커스텀 PropertyEditor 등록
                .build();
    }

    /**
     * 문자열을 LocalDateTime으로 변환하는 커스텀 PropertyEditor 정의
     */
    private PropertyEditor dateTimeEditor() {
        return new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) throws IllegalArgumentException {
                // 사용자 정의 변환 로직 구현 (예: 문자열을 LocalDateTime으로 변환)
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                setValue(LocalDateTime.parse(text, formatter));
            }
        };
    }

    @Bean
    public SystemFailureStdoutItemWriter fixedSystemFailureStdoutItemWriter() {
        return new SystemFailureStdoutItemWriter();
    }

    public static class SystemFailureStdoutItemWriter implements ItemWriter<FixedSystemFailure> {
        @Override
        public void write(Chunk<? extends FixedSystemFailure> chunk) throws Exception {
            for (FixedSystemFailure failure : chunk) {
                log.info("Processing system failure: {}", failure);
            }
        }
    }

    @Data
    public static class FixedSystemFailure {
        private String errorId;
        private String errorDateTime;
        private String severity;
        private Integer processId;
        private String errorMessage;
    }
}

