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
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.PagingQueryProvider;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.batch.item.database.builder.JdbcPagingItemReaderBuilder;
import org.springframework.batch.item.database.support.SqlPagingQueryProviderFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class PagingItemVictimRecordConfig {
    private final JobRepository jobRepository;
    private final PlatformTransactionManager platformTransactionManager;
    private final DataSource dataSource;

    @Bean
    public Job pagingItemVictimJob(Step pagingItemVictimStep) {
        return new JobBuilder("pagingItemVictimJob", jobRepository)
                .start(pagingItemVictimStep)
                .build();
    }

    @Bean
    public Step pagingItemVictimStep(
        JdbcPagingItemReader<Victim> pagingItemVictimReader,
        ItemWriter<Victim> pagingItemVictimWriter
    ) {
        return new StepBuilder("pagingItemVictimStep", jobRepository)
                .<Victim, Victim>chunk(10, platformTransactionManager)
                .reader(pagingItemVictimReader)
                .writer(pagingItemVictimWriter)
                .build();
    }

    /**
     * JdbcPagingItemReaderBuilder 를 사용하여 ItemReader 설정
     */
    @Bean
    public JdbcPagingItemReader<Victim> pagingItemVictimReader() {
        return new JdbcPagingItemReaderBuilder<Victim>()
            .name("pagingItemVictimReader")
            .dataSource(dataSource) //DB 커넥션 설정
            .pageSize(5) //페이지 사이즈 설정 (limit 절) (MARK: Step의 청크 사이즈와 동일하게 설정하는 것을 권장한다. 사이즈가 같다면, 한번의 청크마다 하나의 쿼리가 실행된다.)
            .selectClause("SELECT id, name, process_id, terminated_at, status") //select 절
            .fromClause("FROM victims") //from 절
            .whereClause("WHERE status = :status AND terminated_at <= :terminatedAt") //where 절 (Named Parameter 포함)
            .sortKeys(Map.of("id", Order.ASCENDING)) //정렬 기준 칼럼 설정
            .parameterValues(Map.of(                    // Named Parameter의 값 지정
                "status", "TERMINATED",
                "terminatedAt", LocalDateTime.now()
            )).beanRowMapper(Victim.class) //조회된 데이터를 매핑할 객체 타입
            .build();
    }

    /**
     * 직접 SqlPagingQueryProviderFactoryBean 를 사용하여 ItemReader 설정
     */
    @Bean
    public JdbcPagingItemReader<Victim> pagingItemVictimReaderWithFactoryBean() {
        return new JdbcPagingItemReaderBuilder<Victim>()
            .name("pagingItemVictimReader")
            .dataSource(dataSource) //DB 커넥션 설정
            .pageSize(5) //페이지 사이즈 설정 (limit 절) (MARK: Step의 청크 사이즈와 동일하게 설정하는 것을 권장한다. 사이즈가 같다면, 한번의 청크마다 하나의 쿼리가 실행된다.)
            .queryProvider(pagingQueryProvider(dataSource))
            .parameterValues(Map.of(                    // Named Parameter의 값 지정
                "status", "TERMINATED",
                "terminatedAt", LocalDateTime.now()
            )).beanRowMapper(Victim.class) //조회된 데이터를 매핑할 객체 타입
            .build();
    }

    @Bean
    public ItemWriter<Victim> pagingItemVictimWriter() {
        return items -> {
            for (Victim victim : items) {
                log.info("{}", victim);
            }
        };
    }

    /**
     * JdbcPagingItemReaderBuilder 사용 대신, 직접 Provider 구현
     */
    private PagingQueryProvider pagingQueryProvider(DataSource dataSource) {
        SqlPagingQueryProviderFactoryBean queryProviderFactory = new SqlPagingQueryProviderFactoryBean();

        // 데이터베이스 타입에 맞는 적절한 PagingQueryProvider 구현체를 생성할 수 있도록 dataSource를 전달해줘야 한다.
        queryProviderFactory.setDataSource(dataSource);
        queryProviderFactory.setSelectClause("SELECT id, name, process_id, terminated_at, status");
        queryProviderFactory.setFromClause("FROM victims");
        queryProviderFactory.setWhereClause("WHERE status = :status AND terminated_at <= :terminatedAt");
        queryProviderFactory.setSortKeys(Map.of("id", Order.ASCENDING));

        try {
            return queryProviderFactory.getObject();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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
