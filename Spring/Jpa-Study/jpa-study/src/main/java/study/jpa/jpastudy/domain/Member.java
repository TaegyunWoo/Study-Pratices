package study.jpa.jpastudy.domain;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data /*롬복의 애너테이션*/
@Entity /*본 클래스를 테이블과 매핑한다고 JPA에게 알려줌*/
@Table(name = "MEMBER") /*매핑할 테이블 이름을 알려줌*/
public class Member {

    @Id /*해당 필드를 PK로 사용한다고 알려줌*/
    @Column(name = "ID") /*매핑할 칼럼을 알려줌*/
    private String id;

    @Column(name = "NAME")
    private String username;

    //매핑정보 X
    //매핑 정보가 없다면 'age'라는 컬럼과 매핑된다.
    private Integer age;
}
