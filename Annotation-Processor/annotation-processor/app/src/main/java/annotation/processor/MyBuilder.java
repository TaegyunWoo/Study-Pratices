package annotation.processor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE) //클래스, 인터페이스, enum에 적용
@Retention(RetentionPolicy.SOURCE) //컴파일 시점까지 애너테이션 유지
public @interface MyBuilder {

}
