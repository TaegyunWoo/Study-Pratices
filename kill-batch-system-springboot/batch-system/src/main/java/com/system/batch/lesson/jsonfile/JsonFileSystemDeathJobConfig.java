package com.system.batch.lesson.jsonfile;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.separator.JsonRecordSeparatorPolicy;
import org.springframework.batch.item.json.JacksonJsonObjectMarshaller;
import org.springframework.batch.item.json.JacksonJsonObjectReader;
import org.springframework.batch.item.json.JsonFileItemWriter;
import org.springframework.batch.item.json.JsonItemReader;
import org.springframework.batch.item.json.builder.JsonFileItemWriterBuilder;
import org.springframework.batch.item.json.builder.JsonItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class JsonFileSystemDeathJobConfig {
    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final ObjectMapper objectMapper;

    @Bean
    public Job jsonFileSystemDeathJob(
        Step jsonFileSystemDeathStep
    ) {
        return new JobBuilder("jsonFileSystemDeathJob", jobRepository)
            .start(jsonFileSystemDeathStep)
            .build();
    }

    @Bean
    public Step jsonFileSystemDeathStep(
//        FlatFileItemReader<SystemDeath> jsonFileSystemDeathReader
        JsonItemReader<SystemDeath> jsonFileSystemDeathJsonItemReader,
        JsonFileItemWriter<SystemDeath> jsonFileSystemDeathWriter
    ) {
        return new StepBuilder("jsonFileSystemDeathStep", jobRepository)
            .<SystemDeath, SystemDeath>chunk(10, transactionManager)
            .reader(jsonFileSystemDeathJsonItemReader)
            .writer(jsonFileSystemDeathWriter)
            .build();
    }

    /**
     * FlatFileItemReader 를 통해 JSON 파일 읽기
     */
    @Bean
    @StepScope
    public FlatFileItemReader<SystemDeath> jsonFileSystemDeathReader(
        @Value("#{jobParameters['inputFile']}") String inputFile
    ) {
        return new FlatFileItemReaderBuilder<SystemDeath>()
            .name("jsonFileSystemDeathReader")
            .resource(new FileSystemResource(inputFile))
            .lineMapper((line, lineNumber) -> objectMapper.readValue(line, SystemDeath.class)) //커스텀 라인 매퍼 설정
            .recordSeparatorPolicy(new JsonRecordSeparatorPolicy()) // JSON 경계 감지기 설정 -> 이를 통해 여러 라인에 걸쳐 표현된 하나의 JSON 객체를 구분할 수 있다.
            .build();
    }

    /**
     * JsonItemReader 를 통해 JSON 배열 읽기
     */
    @Bean
    @StepScope
    public JsonItemReader<SystemDeath> jsonFileSystemDeathJsonItemReader(
        @Value("#{jobParameters['inputFile']}") String inputFile
    ) {
        return new JsonItemReaderBuilder<SystemDeath>()
            .name("jsonFileSystemDeathJsonItemReader")
            .resource(new FileSystemResource(inputFile))
            .jsonObjectReader(new JacksonJsonObjectReader<>(SystemDeath.class))
            .build();
    }

    /**
     * Json 객체 형식으로 파일에 쓰는 Writer
     */
    @Bean
    @StepScope
    public JsonFileItemWriter<SystemDeath> jsonFileSystemDeathWriter(
        @Value("#{jobParameters['outputDir']}") String outputDir
    ) {
        return new JsonFileItemWriterBuilder<SystemDeath>()
            .name("jsonFileSystemDeathWriter")
            .resource(new FileSystemResource(outputDir + "/death_notes.json"))
            .jsonObjectMarshaller(new JacksonJsonObjectMarshaller<>()) //객체를 JSON 객체로 변환할 마샬러 설정
            .build();
    }

    public record SystemDeath(String command, int cpu, String status) {}
}
