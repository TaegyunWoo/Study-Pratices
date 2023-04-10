package web.crud.basic.basicweb.form;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FullArticleForm {
    private Long id;
    private String title;
    private String content;
    private Long writerId;
    private String writerName;
    private LocalDateTime dateTime;
}
