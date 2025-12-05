package com.system.batch.lesson.multiresourceitemwriter;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.MultiResourceItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.file.builder.MultiResourceItemWriterBuilder;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.PlatformTransactionManager;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class MultiResourceDeathNoteWriteJobConfig {
    @Bean
    public Job multiResourceDeathNoteWriteJob(
            JobRepository jobRepository,
            Step multiResourceDeathNoteWriteStep
    ) {
        return new JobBuilder("multiResourceDeathNoteWriteJob", jobRepository)
                .start(multiResourceDeathNoteWriteStep)
                .build();
    }

    @Bean
    public Step multiResourceDeathNoteWriteStep(
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager,
            ListItemReader<DeathNote> multiResourceDeathNoteListItemReader,
            MultiResourceItemWriter<DeathNote> multiResourceDeathNoteWriter
    ) {
        return new StepBuilder("multiResourceDeathNoteWriteStep", jobRepository)
                .<DeathNote, DeathNote>chunk(10, transactionManager)
                .reader(multiResourceDeathNoteListItemReader)
                .writer(multiResourceDeathNoteWriter)
                .build();
    }

    /**
     * 기본적인 ListItemReader
     */
    @Bean
    public ListItemReader<DeathNote> multiResourceDeathNoteListItemReader() {
        List<DeathNote> deathNotes = new ArrayList<>();
        for (int i = 1; i <= 15; i++) { // 총 15개의 DeathNote 객체 read()
            String id = String.format("KILL-%03d", i);
            LocalDate date = LocalDate.now().plusDays(i);
            deathNotes.add(new DeathNote(
                id,
                "피해자" + i,
                date.format(DateTimeFormatter.ISO_DATE),
                "처형사유" + i
            ));
        }
        return new ListItemReader<>(deathNotes);
    }

    /**
     * 쓰기 작업을 관리하고 배분하는 MultiResourceItemWriter
     */
    @Bean
    @StepScope
    public MultiResourceItemWriter<DeathNote> multiResourceDeathNoteWriter(
            @Value("#{jobParameters['outputDir']}") String outputDir
    ) {
        return new MultiResourceItemWriterBuilder<DeathNote>()
            .name("multiResourceDeathNoteWriter") //MultiResourceItemWriter의 고유한 이름 지정
            .resource(new FileSystemResource(outputDir + "/death_notes")) //리소스를 지정한다. (실제 쓰기 작업을 하는 delegateItemWriter 에 설정하는 것이 아니다.)
            .itemCountLimitPerResource(10) //한 파일당 최대 라인 수를 지정한다. 해당 데이터 개수가 초과되면 새 파일이 생성된다.
            .delegate(delegateItemWriter()) //실제 파일 쓰기를 수행할 ItemWriter를 설정한다.
            .resourceSuffixCreator(index -> String.format("_%03d.txt", index)) //파일 기본이름 뒤에 붙을 접미사를 정의한다.
            .build();
    }

    /**
     * 실제로 파일에 쓰는 작업을 담당하는 FileItemWriter
     */
    @Bean
    public FlatFileItemWriter<DeathNote> delegateItemWriter() {
        return new FlatFileItemWriterBuilder<DeathNote>()
            .name("multiResourceDeathNoteDelegateWriter")
            .formatted()
            .format("처형 ID: %s | 처형일자: %s | 피해자: %s | 사인: %s")
            .sourceType(DeathNote.class)
            .names("victimId", "executionDate", "victimName", "causeOfDeath")
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
