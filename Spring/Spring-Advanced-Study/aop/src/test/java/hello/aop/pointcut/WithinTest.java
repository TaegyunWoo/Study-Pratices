package hello.aop.pointcut;

import hello.aop.member.MemberServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
public class WithinTest {
  AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
  Method helloMethod;

  @BeforeEach
  public void init() throws NoSuchMethodException {
    helloMethod = MemberServiceImpl.class.getMethod("hello", String.class);
  }

  @Test
  void withinExact() {
    pointcut.setExpression("within(hello.aop.member.MemberServiceImpl)");
    assertTrue(pointcut.matches(helloMethod, MemberServiceImpl.class));
  }

  @Test
  void withinStar() {
    pointcut.setExpression("within(hello.aop.member.*Service*)");
    assertTrue(pointcut.matches(helloMethod, MemberServiceImpl.class));
  }

  @Test
  void withinSubPackage() {
    pointcut.setExpression("within(hello.aop..*)");
    assertTrue(pointcut.matches(helloMethod, MemberServiceImpl.class));
  }

  @DisplayName("타켓의 타입에만 직접 사용, 인터페이스를 선정하면 안된다.")
  @Test
  void withinSuperTypeFalse() {
    pointcut.setExpression("within(hello.aop.member.MemberService)");
    assertFalse(pointcut.matches(helloMethod, MemberServiceImpl.class));
  }

  @DisplayName("execution은 타입 기반, 인터페이스를 선정 가능하다.")
  @Test
  void executionSuperTypeTrue() {
    pointcut.setExpression("execution(* hello.aop.member.MemberService.*(..))");
    assertTrue(pointcut.matches(helloMethod, MemberServiceImpl.class));
  }


}
