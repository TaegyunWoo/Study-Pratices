package web.crud.basic.basicweb.domain;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 사용자 정보 도메인
 */
@Data
public class User {
    private Long id;
    private String email;
    private String name;
    private String password;
}
