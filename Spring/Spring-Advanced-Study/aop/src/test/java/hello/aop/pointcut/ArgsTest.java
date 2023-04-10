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
public class ArgsTest {
  Method helloMethod;

  @BeforeEach
  public void init() throws NoSuchMethodException {
    helloMethod = MemberServiceImpl.class.getMethod("hello", String.class);
  }

  private AspectJExpressionPointcut pointcut(String expression) {
    AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
    pointcut.setExpression(expression);
    return pointcut;
  }

  @Test
  void args() {
    //hello(String)과 매칭
    assertTrue(pointcut("args(String)").matches(helloMethod, MemberServiceImpl.class));
    assertTrue(pointcut("args(Object)").matches(helloMethod, MemberServiceImpl.class));
    assertTrue(pointcut("args(..)").matches(helloMethod, MemberServiceImpl.class));
    assertTrue(pointcut("args(*)").matches(helloMethod, MemberServiceImpl.class));
    assertTrue(pointcut("args(String,..)").matches(helloMethod, MemberServiceImpl.class));
    assertFalse(pointcut("args()").matches(helloMethod, MemberServiceImpl.class));
  }

  /**
   * execution(* *(java.io.Serializable)) : 메서드의 시그니처로 판단 (정적)
   * args(java.io.Serializable) : 런타임에 전달된 인수로 판단 (동적)
   */
  @Test
  void argsVsExecution() {
    //Args
    assertTrue(pointcut("args(String)").matches(helloMethod, MemberServiceImpl.class));
    assertTrue(pointcut("args(java.io.Serializable)").matches(helloMethod, MemberServiceImpl.class));
    assertTrue(pointcut("args(Object)").matches(helloMethod, MemberServiceImpl.class));

    //Execution
    assertTrue(pointcut("execution(* *(String))").matches(helloMethod, MemberServiceImpl.class));
    assertFalse(pointcut("execution(* *(java.io.Serializable))").matches(helloMethod, MemberServiceImpl.class)); //매칭실패
    assertFalse(pointcut("execution(* *(Object))").matches(helloMethod, MemberServiceImpl.class)); //매칭 실패
  }
  
}
