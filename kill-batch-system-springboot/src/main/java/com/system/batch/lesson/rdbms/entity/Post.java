package com.system.batch.lesson.rdbms.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * 게시글 엔티티 - 검열 대상
 */
@Entity
@Table(name = "posts")
@NamedQuery(
    name = "Post.findByReportsReportedAtBetween",
    query = "SELECT p FROM Post p JOIN FETCH p.reports r WHERE r.reportedAt >= :startDateTime AND r.reportedAt < :endDateTime"
)
@Getter
public class Post {
    @Id
    private Long id;
    private String title;         // 게시물 제목
    private String content;       // 게시물 내용
    private String writer;        // 작성자

    @OneToMany(mappedBy = "post")
    private List<Report> reports = new ArrayList<>();
}
