package com.system.batch.lesson.rdbms;

import com.system.batch.lesson.rdbms.entity.Post;
import com.system.batch.lesson.rdbms.entity.Report;
import jakarta.persistence.EntityManagerFactory;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.batch.item.database.orm.JpaNamedQueryProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class JpaPagingItemReaderPostBlockBatchConfig {
    private final JobRepository jobRepository;
    private final PlatformTransactionManager platformTransactionManager;
    private final EntityManagerFactory entityManagerFactory;

    @Bean
    public Job jpaPagingItemReaderPostBlockJob(Step jpaPagingItemReaderPostBlockStep) {
        return new JobBuilder("jpaPagingItemReaderPostBlockJob", jobRepository)
                .start(jpaPagingItemReaderPostBlockStep)
                .build();
    }

    @Bean
    public Step jpaPagingItemReaderPostBlockStep(
        JpaPagingItemReader<Post> jpaPagingItemReaderBlockReader,
        ItemProcessor<Post, JpaPagingItemReaderBlockedPost> jpaPagingItemReaderPostBlockProcessor,
        ItemWriter<JpaPagingItemReaderBlockedPost> jpaPagingItemReaderPostBlockWriter
    ) {
        return new StepBuilder("jpaPagingItemReaderPostBlockStep", jobRepository)
            .<Post, JpaPagingItemReaderBlockedPost>chunk(5, platformTransactionManager)
            .reader(jpaPagingItemReaderBlockReader)
            .processor(jpaPagingItemReaderPostBlockProcessor)
            .writer(jpaPagingItemReaderPostBlockWriter)
            .build();
    }

    @Bean
    @StepScope
    public JpaPagingItemReader<Post> jpaPagingItemReaderBlockReader(
        @Value("#{jobParameters['startDateTime']}") LocalDateTime startDateTime,
        @Value("#{jobParameters['endDateTime']}") LocalDateTime endDateTime
    ) {
        return new JpaPagingItemReaderBuilder<Post>()
            .name("jpaPagingItemReaderBlockReader")
            .entityManagerFactory(entityManagerFactory) //엔티티 매니저 팩토리 설정
//          FETCH JOIN은 사용하지 않는 것이 좋다. 건너뛸 OFFSET 만큼의 데이터를 로드할때, 연관 엔티티를 함께 로드하는 것은 비효율적이기 때문이다.
//          대신 FetchType 을 EAGER 로 설정하고, 배치 사이즈를 조정해서 N+1 문제를 해결할 수 있다.
//          ORDER BY 절을 반드시 사용하여, 페이지를 읽을때의 순서를 보장하자.
            .queryString(
                    """
                    SELECT DISTINCT p FROM Post p 
                    JOIN p.reports r
                    WHERE r.reportedAt >= :startDateTime AND r.reportedAt < :endDateTime
                    ORDER BY p.id ASC
                    """
            )
            .parameterValues(Map.of(
                "startDateTime", startDateTime,
                "endDateTime", endDateTime
            )).pageSize(5) //JpaCursorItemReader 와는 다르게 페이지 크기 설정 가능
            .transacted(false) //JpaPagingItemReader 에서의 예상치 못한 데이터 변경을 막기 위해, 반드시 설정
            .build();

    }

    private JpaNamedQueryProvider<Post> createQueryProvider() {
        JpaNamedQueryProvider<Post> objectJpaNamedQueryProvider = new JpaNamedQueryProvider<>();
        objectJpaNamedQueryProvider.setEntityClass(Post.class);
        objectJpaNamedQueryProvider.setNamedQuery("Post.findByReportsReportedAtBetween");
        return objectJpaNamedQueryProvider;
    }

    @Bean
    public ItemWriter<JpaPagingItemReaderBlockedPost> jpaPagingItemReaderPostBlockWriter() {
        return items -> {
            items.forEach(blockedPost -> {
                log.info("💀 TERMINATED: [ID:{}] '{}' by {} | 신고:{}건 | 점수:{} | kill -9 at {}",
                    blockedPost.getPostId(),
                    blockedPost.getTitle(),
                    blockedPost.getWriter(),
                    blockedPost.getReportCount(),
                    String.format("%.2f", blockedPost.getBlockScore()),
                    blockedPost.getBlockedAt().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
            });
        };
    }

    /**
     * 차단된 게시글 - 처형 결과 보고서
     */
    @Getter
    @Builder
    @ToString
    public static class JpaPagingItemReaderBlockedPost {
        private Long postId;
        private String writer;
        private String title;
        private int reportCount;
        private double blockScore;
        private LocalDateTime blockedAt;
    }

    @Component
    public static class JpaPagingItemReaderPostBlockProcessor implements ItemProcessor<Post, JpaPagingItemReaderBlockedPost> {

        @Override
        public JpaPagingItemReaderBlockedPost process(Post post) {
            // 각 신고의 신뢰도를 기반으로 차단 점수 계산
            double blockScore = calculateBlockScore(post.getReports());

            // 차단 점수가 기준치를 넘으면 처형 결정
            if (blockScore >= 7.0) {
                return JpaPagingItemReaderBlockedPost.builder()
                    .postId(post.getId())
                    .writer(post.getWriter())
                    .title(post.getTitle())
                    .reportCount(post.getReports().size())
                    .blockScore(blockScore)
                    .blockedAt(LocalDateTime.now())
                    .build();
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
