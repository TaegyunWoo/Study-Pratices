package com.system.batch.lesson.rdbms;

import com.system.batch.lesson.rdbms.entity.BlockedPost;
import com.system.batch.lesson.rdbms.entity.Post;
import com.system.batch.lesson.rdbms.entity.Report;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaCursorItemReader;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.builder.JpaCursorItemReaderBuilder;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.batch.item.database.orm.JpaNamedQueryProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class JpaItemWriterMergePostBlockBatchConfig {
    private final JobRepository jobRepository;
    private final PlatformTransactionManager platformTransactionManager;
    private final EntityManagerFactory entityManagerFactory;

    @Bean
    public Job jpaItemWriterMergePostBlockJob(Step jpaItemWriterMergePostBlockStep) {
        return new JobBuilder("jpaItemWriterMergePostBlockJob", jobRepository)
                .start(jpaItemWriterMergePostBlockStep)
                .build();
    }

    @Bean
    public Step jpaItemWriterMergePostBlockStep(
        JpaCursorItemReader<Post> jpaItemWriterBlockReader,
        ItemProcessor<Post, Post> jpaItemWriterMergePostBlockProcessor,
        ItemWriter<Post> jpaItemWriterMergePostBlockWriter
    ) {
        return new StepBuilder("jpaItemWriterMergePostBlockStep", jobRepository)
            .<Post, Post>chunk(5, platformTransactionManager)
            .reader(jpaItemWriterBlockReader)
            .processor(jpaItemWriterMergePostBlockProcessor)
            .writer(jpaItemWriterMergePostBlockWriter)
            .build();
    }

    /**
     * 본 Job과 같이 기존 엔티티를 수정하는 경우, JpaCursorItemReader 사용이 권장된다.
     * JpaPagingItemReader 는 데이터를 읽기 전에, 내부적으로 entityManager.flush() 를 수행하기에 의도치않은 데이터 변경이 일어날 수 있다.
     */
    @Bean
    @StepScope
    public JpaCursorItemReader<Post> jpaItemWriterMergeBlockReader(
        @Value("#{jobParameters['startDateTime']}") LocalDateTime startDateTime,
        @Value("#{jobParameters['endDateTime']}") LocalDateTime endDateTime
    ) {
        return new JpaCursorItemReaderBuilder<Post>()
            .name("jpaItemWriterBlockReader")
            .entityManagerFactory(entityManagerFactory) //엔티티 매니저 팩토리 설정
//            [쿼리 스트링 방식]
//            .queryString( //JPQL 설정
//                """
//                SELECT p FROM Post p JOIN FETCH p.reports r
//                                    WHERE r.reportedAt >= :startDateTime AND r.reportedAt < :endDateTime
//                """
//            )
//          [쿼리 프로바이더 방식]
            .queryProvider(createQueryProvider()) //query provider 설정
            .hintValues(Map.of("org.hibernate.fetchSize", 5)) //JPA 기반의 ItemReader 에는 fetchSize 설정 메서드가 없기에, 이와 같이 힌트로 설정할 수 있다.
            .parameterValues(Map.of(
                "startDateTime", startDateTime,
                "endDateTime", endDateTime
            )).build();

    }

    private JpaNamedQueryProvider<Post> createQueryProvider() {
        JpaNamedQueryProvider<Post> objectJpaNamedQueryProvider = new JpaNamedQueryProvider<>();
        objectJpaNamedQueryProvider.setEntityClass(Post.class);
        objectJpaNamedQueryProvider.setNamedQuery("Post.findByReportsReportedAtBetween");
        return objectJpaNamedQueryProvider;
    }

    @Bean
    public JpaItemWriter<Post> jpaItemWriterMergePostBlockWriter() {
        return new JpaItemWriterBuilder<Post>()
            .entityManagerFactory(entityManagerFactory)
//          .usePersist(true) //엔티티 저장시, persist() 를 사용하도록 설정
            .usePersist(false) //엔티티 저장시, merge() 를 사용하도록 설정 (기존 엔티티를 수정하는 경우 사용)
            .build();
    }

    @Component
    public static class JpaItemWriterMergePostBlockProcessor implements ItemProcessor<Post, Post> {

        @Override
        public Post process(Post post) {
            // 각 신고의 신뢰도를 기반으로 차단 점수 계산
            double blockScore = calculateBlockScore(post.getReports());

            // 차단 점수가 기준치를 넘으면 처형 결정
            if (blockScore >= 7.0) {
                post.addBlockedAt(LocalDateTime.now());
                return post;
            }

            return null;  // 무죄 방면
        }

        private double calculateBlockScore(List<Report> reports) {
            // 각 신고들의 정보를 시그니처에 포함시켜 마치 사용하는 것처럼 보이지만...
            for (Report report : reports) {
                analyzeReportType(report.getReportType());            // 신고 유형 분석
                checkReporterTrust(report.getReporterLevel());        // 신고자 신뢰도 확인
                validateEvidence(report.getEvidenceData());           // 증거 데이터 검증
                calculateTimeValidity(report.getReportedAt());        // 시간 가중치 계산
            }

            // 실제로는 그냥 랜덤 값을 반환
            return Math.random() * 10;  // 0~10 사이의 랜덤 값
        }

        // 아래는 실제로는 아무것도 하지 않는 메서드들
        private void analyzeReportType(String reportType) {
            // 신고 유형 분석하는 척
        }

        private void checkReporterTrust(int reporterLevel) {
            // 신고자 신뢰도 확인하는 척
        }

        private void validateEvidence(String evidenceData) {
            // 증거 검증하는 척
        }

        private void calculateTimeValidity(LocalDateTime reportedAt) {
            // 시간 가중치 계산하는 척
        }
    }
}
