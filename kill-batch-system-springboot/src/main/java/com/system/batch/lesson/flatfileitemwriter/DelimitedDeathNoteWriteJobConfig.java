package com.system.batch.lesson.flatfileitemwriter;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.file.transform.FieldExtractor;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.List;

@Configuration
public class DelimitedDeathNoteWriteJobConfig {
    @Bean
    public Job delimitedDeathNoteWriteJob(
            JobRepository jobRepository,
            Step delimitedDeathNoteWriteStep
    ) {
        return new JobBuilder("delimitedDeathNoteWriteJob", jobRepository)
                .start(delimitedDeathNoteWriteStep)
                .build();
    }

    @Bean
    public Step delimitedDeathNoteWriteStep(
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager,
            ListItemReader<DeathNote> delimitedDeathNoteListItemReader,
            FlatFileItemWriter<DeathNote> delimitedDeathNoteWriter
    ) {
        return new StepBuilder("deathNoteWriteStep", jobRepository)
                .<DeathNote, DeathNote>chunk(10, transactionManager)
                .reader(delimitedDeathNoteListItemReader)
                .writer(delimitedDeathNoteWriter)
                .build();
    }

    /**
     * 기본적인 ListItemReader
     */
    @Bean
    public ListItemReader<DeathNote> delimitedDeathNoteListItemReader() {
        List<DeathNote> victims = List.of(
                new DeathNote(
                        "KILL-001",
                        "김배치",
                        "2024-01-25",
                        "CPU 과부하"),
                new DeathNote(
                        "KILL-002",
                        "사불링",
                        "2024-01-26",
                        "JVM 스택오버플로우"),
                new DeathNote(
                        "KILL-003",
                        "박탐묘",
                        "2024-01-27",
                        "힙 메모리 고갈")
        );

        return new ListItemReader<>(victims);
    }

    /**
     * Delimited(구분자) 방식의 FlatFileItemWriter
     */
    @Bean
    @StepScope
    public FlatFileItemWriter<DeathNote> delimitedDeathNoteWriter(
            @Value("#{jobParameters['outputDir']}") String outputDir
    ) {
        return new FlatFileItemWriterBuilder<DeathNote>()
                .name("delimitedDeathNoteWriter") //고유 이름 설정
                .resource(new FileSystemResource(outputDir + "deatch_note.csv")) // 출력 파일 경로 설정
                .delimited() // 사용할 구현체로 DelimitedLineAggregator 지정
                .delimiter(",") // 구분자 설정
                /*
                 아래와 같이 직접 FieldExtractor를 구현할 수도 있지만,
                 sourceType() 메서드를 사용해 도메인 객체의 타입을 전달하면, 자동으로 객체 타입에 맞는 적절한 FieldExtractor가 사용된다.
                 본 예시에서는 생략하겠다.

                .fieldExtractor(deathNoteItem -> {
                    return new Object[]{
                            deathNoteItem.getVictimId(),
                            deathNoteItem.getVictimName(),
                            deathNoteItem.getExecutionDate(),
                            deathNoteItem.getCauseOfDeath()
                    };
                })
                 */
                .sourceType(DeathNote.class) // 도메인 객체 타입 지정, 자동으로 BeanWrapperFieldExtractor 가 사용된다. 하지만, sourceType() 메서드도 생략 가능하다.
                .names("victimId", "victimName", "executionDate", "causeOfDeath") // 도메인 객체의 필드 중, 추출할 필드명 지정한다. 해당 필드명은 getter 메서드의 이름과 일치해야 한다. (여기에 설정한 순서대로 출력된다.)
                .headerCallback(writer -> writer.write("처형ID,피해자명,처형일자,사인")) // 헤더 설정
                .footerCallback(writer -> writer.write("===  끝  ===")) // 푸터 설정
                .build();
    }

    @Data
    @AllArgsConstructor
    public static class DeathNote {
        private String victimId;
        private String victimName;
        private String executionDate;
        private String causeOfDeath;
    }
}
