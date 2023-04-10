package web.crud.basic.basicweb.form;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
public class NewArticleForm {

    @NotBlank
    private String title;

    @NotBlank
    private String content;
}
