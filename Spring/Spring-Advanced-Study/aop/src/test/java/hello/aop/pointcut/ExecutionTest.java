package hello.aop.pointcut;

import hello.aop.member.MemberServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
public class ExecutionTest {
  AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
  Method helloMethod;

  @BeforeEach
  public void init() throws NoSuchMethodException {
    helloMethod = MemberServiceImpl.class.getMethod("hello", String.class);
  }

  @Test
  void printMethod() {
    /* 출력결과
     * public java.lang.String hello.aop.member.MemberServiceImpl.hello(java.lang.String)
     */
    log.info("helloMethod={}", helloMethod);
  }

  @Test
  void exactMatch() {
    //public java.lang.String hello.aop.member.MemberServiceImpl.hello(java.lang.String)
    pointcut.setExpression("execution(public String hello.aop.member.MemberServiceImpl.hello(String))");
    assertTrue(pointcut.matches(helloMethod, MemberServiceImpl.class));
  }

  @Test
  void allMatch() {
    pointcut.setExpression("execution(* *(..))");
    assertTrue(pointcut.matches(helloMethod, MemberServiceImpl.class));
  }

  @Test
  void nameMatch() {
    pointcut.setExpression("execution(* hello(..))");
    assertTrue(pointcut.matches(helloMethod, MemberServiceImpl.class));
  }

  @Test
  void nameMatchStar1() {
    pointcut.setExpression("execution(* hel*(..))");
    assertTrue(pointcut.matches(helloMethod, MemberServiceImpl.class));
  }

  @Test
  void nameMatchStar2() {
    pointcut.setExpression("execution(* *el*(..))");
    assertTrue(pointcut.matches(helloMethod, MemberServiceImpl.class));
  }

  @Test
  void nameMatchFalse() {
    pointcut.setExpression("execution(* nono(..))");
    assertFalse(pointcut.matches(helloMethod, MemberServiceImpl.class));
  }

  @Test
  void packageExactMatch1() {
    pointcut.setExpression("execution(* hello.aop.member.MemberServiceImpl.hello(..))");
    assertTrue(pointcut.matches(helloMethod, MemberServiceImpl.class));
  }

  @Test
  void packageExactMatch2() {
    pointcut.setExpression("execution(* hello.aop.member.*.*(..))");
    assertTrue(pointcut.matches(helloMethod, MemberServiceImpl.class));
  }

  @Test
  void packageExactMatchFalse() {
    pointcut.setExpression("execution(* hello.aop.*.*(..))");
    assertFalse(pointcut.matches(helloMethod, MemberServiceImpl.class));
  }

  @Test
  void packageMatchSubPackage1() {
    pointcut.setExpression("execution(* hello.aop.member..*.*(..))");
    assertTrue(pointcut.matches(helloMethod, MemberServiceImpl.class));
  }

  @Test
  void packageMatchSubPackage2() {
    pointcut.setExpression("execution(* hello.aop..*.*(..))");
    assertTrue(pointcut.matches(helloMethod, MemberServiceImpl.class));
  }

  @Test
  void typeExactMatch() {
    pointcut.setExpression("execution(* hello.aop.member.MemberServiceImpl.*(..))");
    assertTrue(pointcut.matches(helloMethod, MemberServiceImpl.class));
  }

  @Test
  void typeMatchSuperType() {
    pointcut.setExpression("execution(* hello.aop.member.MemberService.*(..))");
    assertTrue(pointcut.matches(helloMethod, MemberServiceImpl.class));
  }

  @Test
  void typeMatchInternal() throws NoSuchMethodException {
    pointcut.setExpression("execution(* hello.aop.member.MemberServiceImpl.*(..))");
    Method internalMethod = MemberServiceImpl.class.getMethod("internal", String.class);
    assertTrue(pointcut.matches(internalMethod, MemberServiceImpl.class));
  }

  //포인트컷으로 지정한 MemberService 는 internal 이라는 이름의 메서드가 없다.
  @Test
  void typeMatchNoSuperTypeMethodFalse() throws NoSuchMethodException {
    pointcut.setExpression("execution(* hello.aop.member.MemberService.*(..))");
    Method internalMethod = MemberServiceImpl.class.getMethod("internal", String.class);
    assertFalse(pointcut.matches(internalMethod, MemberServiceImpl.class));
  }

  //String 타입의 파라미터 허용
  //(String)
  @Test
  void argsMatch() {
    pointcut.setExpression("execution(* *(String))");
    assertTrue(pointcut.matches(helloMethod, MemberServiceImpl.class));
  }

  //파라미터가 없어야 함
  //()
  @Test
  void argsMatchNoArgs() {
    pointcut.setExpression("execution(* *())");
    assertFalse(pointcut.matches(helloMethod, MemberServiceImpl.class));
  }

  //정확히 하나의 파라미터 허용, 모든 타입 허용
  //(Xxx)
  @Test
  void argsMatchStar() {
    pointcut.setExpression("execution(* *(*))");
    assertTrue(pointcut.matches(helloMethod, MemberServiceImpl.class));
  }

  //파라미터 개수와 무관하게 모든 파라미터, 모든 타입 허용
  //파라미터가 없어도 됨
  //(), (Xxx), (Xxx, Xxx)
  @Test
  void argsMatchAll() {
    pointcut.setExpression("execution(* *(..))");
    assertTrue(pointcut.matches(helloMethod, MemberServiceImpl.class));
  }

  //String 타입으로 시작, 파라미터 개수와 무관하게 모든 파라미터, 모든 타입 허용
  //(String), (String, Xxx), (String, Xxx, Xxx) 허용
  @Test
  void argsMatchComplex() {
    pointcut.setExpression("execution(* *(String, ..))");
    assertTrue(pointcut.matches(helloMethod, MemberServiceImpl.class));
  }
}
