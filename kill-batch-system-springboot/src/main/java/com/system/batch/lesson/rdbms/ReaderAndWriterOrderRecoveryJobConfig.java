package com.system.batch.lesson.rdbms;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.database.builder.JdbcPagingItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class ReaderAndWriterOrderRecoveryJobConfig {
    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final DataSource dataSource;

    @Bean
    public Job readerAndWriterOrderRecoveryJob(Step readerAndWriterOrderRecoveryStep) {
        return new JobBuilder("readerAndWriterOrderRecoveryJob", jobRepository)
            .start(readerAndWriterOrderRecoveryStep)
            .build();
    }

    @Bean
    public Step readerAndWriterOrderRecoveryStep(
        JdbcPagingItemReader<HackedOrder> reader,
        ItemProcessor<HackedOrder, HackedOrder> processor,
        JdbcBatchItemWriter<HackedOrder> writer
    ) {
        return new StepBuilder("readerAndWriterOrderRecoveryStep", jobRepository)
            .<HackedOrder, HackedOrder>chunk(10, transactionManager)
            .reader(reader)
            .processor(processor)
            .writer(writer)
            .build();
    }

    /**
     * JdbcPagingItemReader 를 활용한 RDB 읽기
     */
    @Bean
    public JdbcPagingItemReader<HackedOrder> readerAndWriterOrderRecoveryItemReader() {
        return new JdbcPagingItemReaderBuilder<HackedOrder>()
            .name("readerAndWriterOrderRecoveryItemReader")
            .dataSource(dataSource)
            .pageSize(5)
            .selectClause("SELECT id, customer_id, order_datetime, status, shipping_id")
            .fromClause("FROM orders")
            .whereClause(
                """
                WHERE (status = 'SHIPPED' and shipping_id is null)
                  or (status = 'CANCELLED' and shipping_id is not null)
                """
            ).sortKeys(Map.of("id", Order.ASCENDING))
            .beanRowMapper(HackedOrder.class)
            .build();
    }

    /**
     * ItemProcessor
     */
    @Bean
    public ItemProcessor<HackedOrder, HackedOrder> itemProcessor() {
        return order -> {
            if (order.getShippingId() == null) {
                order.setStatus("READY_FOR_SHIPMENT");
            } else {
                order.setStatus("SHIPPED");
            }
            return order;
        };
    }

    /**
     * JdbcBatchItemWriter 를 활용한 RDB 쓰기
     */
    @Bean
    public JdbcBatchItemWriter<HackedOrder> readerAndWriterOrderRecoveryItemWriter() {
        return new JdbcBatchItemWriterBuilder<HackedOrder>()
            .dataSource(dataSource)
            .sql("UPDATE orders SET status = :status WHERE id = :id")
            .beanMapped()
            .assertUpdates(true) //하나의 데이터라도 업데이트에 실패하면 즉시 예외를 던져 프로세스를 중단한다.
            .build();
    }

    @Data
    @NoArgsConstructor
    public static class HackedOrder {
        private Long id;
        private Long customerId;
        private LocalDateTime orderDateTime;
        private String status;
        private String shippingId;
    }
}
