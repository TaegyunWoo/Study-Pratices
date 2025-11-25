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
import org.springframework.batch.item.file.MultiResourceItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.builder.MultiResourceItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.transaction.PlatformTransactionManager;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class MultiResourceSystemFailureJobConfig {
    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;

    /**
     * Job 설정
     */
    @Bean
    public Job multiResourceSystemFailureJob(Step multiResourceSystemFailureStep) {
        return new JobBuilder("multiResourceSystemFailureJob", jobRepository)
            .start(multiResourceSystemFailureStep)
            .build();
    }

    /**
     * Step 설정
     */
    @Bean
    public Step multiResourceSystemFailureStep(
        MultiResourceItemReader<MultiResourceSystemFailure> multiSystemFailureItemReader,
        MultiResourceSystemFailureStdoutItemWriter multiResourceSystemFailureStdoutItemWriter
    ) {
        return new StepBuilder("multiResourceSystemFailureStep", jobRepository)
            .<MultiResourceSystemFailure, MultiResourceSystemFailure>chunk(10, transactionManager)
            .reader(multiSystemFailureItemReader)
            .writer(multiResourceSystemFailureStdoutItemWriter)
            .build();
    }

    /**
     * MultiResourceItemReader 정의
     */
    @Bean
    @StepScope
    public MultiResourceItemReader<MultiResourceSystemFailure> multiResourceItemReader(
        @Value("#{jobParameters['inputFilePath']}") String inputFilePath
    ) {
        return new MultiResourceItemReaderBuilder<MultiResourceSystemFailure>()
            .name("multiSystemFailureItemReader")
            .resources(new Resource[] {
                new FileSystemResource(inputFilePath + "/critical-failures.csv"), //읽을 파일 1
                new FileSystemResource(inputFilePath + "/normal-failures.csv") //읽을 파일 2
            }).delegate(multiResourceSystemFailureItemReader()) //실제로 파일을 읽는 것은 실제 ItemReader 에게 위임
            .comparator((r1, r2) -> { //읽을 파일 순서 정의 (정의하지 않으면 파일명 알파벳순으로 읽음)
                // 파일명 역순으로 정렬 (normal -> critical 순서)
                return r2.getFilename().compareTo(r1.getFilename());
            })
            .build();
    }

    /**
     * FlatFileItemReader 설정
     */
    @Bean
    public FlatFileItemReader<MultiResourceSystemFailure> multiResourceSystemFailureItemReader() {
        return new FlatFileItemReaderBuilder<MultiResourceSystemFailure>()
            .name("multiResourceSystemFailureItemReader") //FlatFileItemReader 의 유니크한 이름 설정
            .delimited() //읽어들일 파일이 구분자로 분리된 형식임을 알림 -> DelimitedLineTokenizer 가 사용됨.
            .delimiter(",") //구분자 지정
            .names( //매핑할 필드명
                "errorId",          //첫번째 토큰을 매핑할 필드명
                "errorDateTime",    //두번째 토큰을 매핑할 필드명
                "severity",         //세번째 토큰을 매핑할 필드명
                "processId",        //네번째 토큰을 매핑할 필드명
                "errorMessage"      //다섯번째 토큰을 매핑할 필드명
            ).targetType(MultiResourceSystemFailure.class) //최종적으로 매핑될 객체 타입 (이미 제네릭으로 타입을 명시했지만, 컴파일 이후 소거되므로 런타임에 사용할 수 있게끔 설정 필요)
            .linesToSkip(1) //생략할 라인 번호 (첫번째 라인에 있는 필드명 정보는 생략)
            .build();
    }

    @Bean
    public MultiResourceSystemFailureStdoutItemWriter multiResourceSystemFailureStdoutItemWriter() {
        return new MultiResourceSystemFailureStdoutItemWriter();
    }

    /**
     * 단순히 로그로 찍는 Writer
     */
    public static class MultiResourceSystemFailureStdoutItemWriter implements ItemWriter<MultiResourceSystemFailure> {
        @Override
        public void write(Chunk<? extends MultiResourceSystemFailure> chunk) throws Exception {
            for (MultiResourceSystemFailure failure : chunk) {
                log.info("Processing system failure: {}", failure);
            }
        }
    }

    @Data
    public static class MultiResourceSystemFailure {
        private String errorId;
        private String errorDateTime;
        private String severity;
        private Integer processId;
        private String errorMessage;
    }
}
