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
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.List;

@Configuration
public class FormatterDeathNoteWriteJobConfig {
    @Bean
    public Job formatterDeathNoteWriteJob(
            JobRepository jobRepository,
            Step formatterDeathNoteWriteStep
    ) {
        return new JobBuilder("formatterDeathNoteWriteJob", jobRepository)
                .start(formatterDeathNoteWriteStep)
                .build();
    }

    @Bean
    public Step formatterDeathNoteWriteStep(
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager,
            ListItemReader<DeathNote> formatterDeathNoteListItemReader,
            FlatFileItemWriter<DeathNote> formatterDeathNoteWriter
    ) {
        return new StepBuilder("formatterDeathNoteWriteStep", jobRepository)
                .<DeathNote, DeathNote>chunk(10, transactionManager)
                .reader(formatterDeathNoteListItemReader)
                .writer(formatterDeathNoteWriter)
                .build();
    }

    /**
     * 기본적인 ListItemReader
     */
    @Bean
    public ListItemReader<DeathNote> formatterDeathNoteListItemReader() {
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
     * Formatter(커스텀 포맷) 방식의 FlatFileItemWriter
     */
    @Bean
    @StepScope
    public FlatFileItemWriter<DeathNote> formatterDeathNoteWriter(
            @Value("#{jobParameters['outputDir']}") String outputDir
    ) {
        return new FlatFileItemWriterBuilder<DeathNote>()
            .name("formatterDeathNoteWriter") //고유 이름 설정
            .resource(new FileSystemResource(outputDir + "deatch_note.csv")) // 출력 파일 경로 설정
            .formatted() //FlatFileItemWriter 가 사용할 LineAggregator 의 구현체로 FormatterLineAggregator 를 지정한다.
            .format("처형 ID: %s | 처형일자: %s | 피해자: %s | 사인: %s") //FormatterLineAggregator 가 사용할 포맷 문자열을 지정한다.
            .sourceType(DeathNote.class)
            .names("victimId", "executionDate", "causeOfDeath", "victimName")
            .headerCallback(writer -> writer.write("================= 처형 기록부 ================="))
            .footerCallback(writer -> writer.write("================= 처형 완료 =================="))
            .build();
    }

    /**
     * Formatter(커스텀 포맷) 방식의 FlatFileItemWriter (Record 사용)
     */
    @Bean
    @StepScope
    public FlatFileItemWriter<DeathNoteRecord> formatterDeathNoteRecordWriter(
        @Value("#{jobParameters['outputDir']}") String outputDir
    ) {
        return new FlatFileItemWriterBuilder<DeathNoteRecord>()
            .name("formatterDeathNoteRecordWriter") //고유 이름 설정
            .resource(new FileSystemResource(outputDir + "deatch_note.csv")) // 출력 파일 경로 설정
            .formatted() //FlatFileItemWriter 가 사용할 LineAggregator 의 구현체로 FormatterLineAggregator 를 지정한다.
            .format("처형 ID: %s | 처형일자: %s | 피해자: %s | 사인: %s") //FormatterLineAggregator 가 사용할 포맷 문자열을 지정한다.
            .sourceType(DeathNoteRecord.class)
            .names("victimId", "executionDate", "causeOfDeath", "victimName")
            .headerCallback(writer -> writer.write("================= 처형 기록부 ================="))
            .footerCallback(writer -> writer.write("================= 처형 완료 =================="))
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

    public record DeathNoteRecord(String victimId, String victimName, String executionDate, String causeOfDeath) { }
}
