package web.crud.basic.basicweb.form;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class LoginForm {

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Pattern(regexp = FormFieldPattern.PASSWORD_PATTERN)
    private String password;

}
