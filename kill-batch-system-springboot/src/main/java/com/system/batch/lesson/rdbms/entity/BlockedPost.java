package com.system.batch.lesson.rdbms.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "blocked_posts")
@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class BlockedPost {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "blocked_posts_id_seq")
    @SequenceGenerator(name = "blocked_posts_id_seq", sequenceName = "blocked_posts_id_seq", allocationSize = 100) //DB 시퀀스의 INCREMENT BY 설정과 동일하게 맞춰야 한다.
    private Long id;

    @Column(name = "post_id")
    private Long postId;

    private String writer;
    private String title;

    @Column(name = "report_count")
    private int reportCount;

    @Column(name = "block_score")
    private double blockScore;

    @Column(name = "blocked_at")
    private LocalDateTime blockedAt;

    @Builder
    public BlockedPost(Long postId, String writer, String title,
                       int reportCount, double blockScore, LocalDateTime blockedAt) {
        this.postId = postId;
        this.writer = writer;
        this.title = title;
        this.reportCount = reportCount;
        this.blockScore = blockScore;
        this.blockedAt = blockedAt;
    }
}
