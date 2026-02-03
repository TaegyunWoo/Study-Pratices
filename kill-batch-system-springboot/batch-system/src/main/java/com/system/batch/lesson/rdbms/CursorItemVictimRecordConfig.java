package com.system.batch.lesson.rdbms;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class CursorItemVictimRecordConfig {
    private final JobRepository jobRepository;
    private final PlatformTransactionManager platformTransactionManager;
    private final DataSource dataSource;

    @Bean
    public Job cursorItemVictimJob(Step cursorItemVictimStep) {
        return new JobBuilder("cursorItemVictimJob", jobRepository)
                .start(cursorItemVictimStep)
                .build();
    }

    @Bean
    public Step cursorItemVictimStep(
        JdbcCursorItemReader<Victim> cursorItemVictimReader,
        ItemWriter<Victim> cursorItemVictimWriter
    ) {
        return new StepBuilder("cursorItemVictimStep", jobRepository)
                .<Victim, Victim>chunk(10, platformTransactionManager)
                .reader(cursorItemVictimReader)
                .writer(cursorItemVictimWriter)
                .build();
    }

    /**
     * DB 읽기 설정
     */
    @Bean
    public JdbcCursorItemReader<Victim> cursorItemVictimReader() {
        return new JdbcCursorItemReaderBuilder<Victim>()
            .name("cursorItemVictimReader")
            .dataSource(dataSource) //DB 커넥션 설정
            .sql("SELECT * FROM victims WHERE status = ? AND terminated_at <= ?") //바인딩 파라미터를 포함할 수 있다.
            .queryArguments(List.of("TERMINATED", LocalDateTime.now())) //순서대로 바인딩된다.
//            .beanRowMapper(Victim.class) //읽은 ResultSet 을 `BeanPropertyRowMapper` 로 매핑할 객체 설정
//            .dataRowMapper(VictimRecord.class) //불변 객체로도 매핑할 수 있다.
            .rowMapper((rs, rowNum) -> { //커스텀 매퍼로 매핑할 수도 있다.
                Victim victim = new Victim();
                victim.setId(rs.getLong("id"));
                victim.setName(rs.getString("name"));
                victim.setProcessId(rs.getString("process_id"));
                victim.setTerminatedAt(rs.getTimestamp("terminated_at").toLocalDateTime());
                victim.setStatus(rs.getString("status"));
                return victim;
            })
            .fetchSize(10) // 해당 설정을 통해, 한번 쿼리 실행시 ResultSet 의 내부버퍼에 읽어올 row 개수를 조정할 수 있다.
            .build();
    }

    @Bean
    public ItemWriter<Victim> cursorItemVictimWriter() {
        return items -> {
            for (Victim victim : items) {
                log.info("{}", victim);
            }
        };
    }

    @NoArgsConstructor
    @Data
    public static class Victim {
        private Long id;
        private String name;
        private String processId;
        private LocalDateTime terminatedAt;
        private String status;
    }

    public record VictimRecord(Long id, String name, String processId,
                         LocalDateTime terminatedAt, String status) {}
}
