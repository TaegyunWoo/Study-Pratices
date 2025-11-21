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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.PlatformTransactionManager;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class SystemFailureJobConfig {
    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;

    /**
     * Job 설정
     */
    @Bean
    public Job systemFailureJob(Step systemFailureStep) {
        return new JobBuilder("systemFailureJob", jobRepository)
            .start(systemFailureStep)
            .build();
    }

    /**
     * Step 설정
     */
    @Bean
    public Step systemFailureStep(
        FlatFileItemReader<SystemFailure> systemFailureItemReader,
        SystemFailureStdoutItemWriter systemFailureStdoutItemWriter
    ) {
        return new StepBuilder("systemFailureStep", jobRepository)
            .<SystemFailure, SystemFailure>chunk(10, transactionManager)
            .reader(systemFailureItemReader)
            .writer(systemFailureStdoutItemWriter)
            .build();
    }

    /**
     * FlatFileItemReader 설정
     */
    @Bean
    @StepScope
    public FlatFileItemReader<SystemFailure> systemFailureItemReader(
        @Value("#{jobParameters['inputFile']}") String inputFile
    ) {
        return new FlatFileItemReaderBuilder<SystemFailure>()
            .name("systemFailureItemReader") //FlatFileItemReader 의 유니크한 이름 설정
            .resource(new FileSystemResource(inputFile)) //읽을 파일(리소스) 설정
            .delimited() //읽어들일 파일이 구분자로 분리된 형식임을 알림 -> DelimitedLineTokenizer 가 사용됨.
            .delimiter(",") //구분자 지정
            .names( //매핑할 필드명
                "errorId",          //첫번째 토큰을 매핑할 필드명
                "errorDateTime",    //두번째 토큰을 매핑할 필드명
                "severity",         //세번째 토큰을 매핑할 필드명
                "processId",        //네번째 토큰을 매핑할 필드명
                "errorMessage"      //다섯번째 토큰을 매핑할 필드명
            ).targetType(SystemFailure.class) //최종적으로 매핑될 객체 타입 (이미 제네릭으로 타입을 명시했지만, 컴파일 이후 소거되므로 런타임에 사용할 수 있게끔 설정 필요)
            .linesToSkip(1) //생략할 라인 번호 (첫번째 라인에 있는 필드명 정보는 생략)
            .build();
    }

    @Bean
    public SystemFailureStdoutItemWriter systemFailureStdoutItemWriter() {
        return new SystemFailureStdoutItemWriter();
    }

    /**
     * 단순히 로그로 찍는 Writer
     */
    public static class SystemFailureStdoutItemWriter implements ItemWriter<SystemFailure> {
        @Override
        public void write(Chunk<? extends SystemFailure> chunk) throws Exception {
            for (SystemFailure failure : chunk) {
                log.info("Processing system failure: {}", failure);
            }
        }
    }

    @Data
    public static class SystemFailure {
        private String errorId;
        private String errorDateTime;
        private String severity;
        private Integer processId;
        private String errorMessage;
    }
}
