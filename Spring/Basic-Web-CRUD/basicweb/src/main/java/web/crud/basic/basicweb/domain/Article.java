package web.crud.basic.basicweb.domain;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 게시글 도메인
 */

@Data
public class Article {
    private Long id;
    private String title;
    private String content;
    private Long writer;
    private LocalDateTime dateTime;
}
