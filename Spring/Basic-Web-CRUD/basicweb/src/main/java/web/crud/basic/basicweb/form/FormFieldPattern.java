package web.crud.basic.basicweb.form;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
public class FormFieldPattern {

    protected final static String PASSWORD_PATTERN = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d~!@#$%^&*()+|=]{8,20}$";

}
