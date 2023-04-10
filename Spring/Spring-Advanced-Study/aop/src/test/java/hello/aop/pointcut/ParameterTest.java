package hello.aop.pointcut;

import hello.aop.member.MemberService;
import hello.aop.member.annotation.ClassAop;
import hello.aop.member.annotation.MethodAop;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Slf4j
@Import(ParameterTest.ParameterAspect.class)
@SpringBootTest
public class ParameterTest {

  @Autowired
  MemberService memberService;

  @Test
  void success() {
    log.info("memberService Proxy={}", memberService.getClass());
    memberService.hello("helloA");
  }

  @Slf4j
  @Aspect
  static class ParameterAspect {

    @Pointcut("execution(* hello.aop.member..*.*(..))")
    private void allMember() {}

    @Around("allMember()")
    public Object logArgs1(ProceedingJoinPoint joinPoint) throws Throwable {
      Object arg1 = joinPoint.getArgs()[0];
      log.info("[logArgs1]{}, arg={}", joinPoint.getSignature(), arg1);
      return joinPoint.proceed();
    }

    //조인포인트 메서드에 전달될 Argument 를 전달받음
    @Around("allMember() && args(arg,..)")
    public Object logArgs2(ProceedingJoinPoint joinPoint, Object arg) throws Throwable {
      Object arg1 = joinPoint.getArgs()[0];
      log.info("[logArgs2]{}, arg={}", joinPoint.getSignature(), arg);
      return joinPoint.proceed();
    }

    //조인포인트 메서드에 전달될 Argument 를 전달받음
    @Before("allMember() && args(arg,..)")
    public void logArgs3(String arg) {
      log.info("[logArgs3] arg={}", arg);
    }

    //프록시 객체를 전달받음
    @Before("allMember() && this(obj)")
    public void thisArgs(JoinPoint joinPoint, MemberService obj) {
      log.info("[this]{}, obj={}", joinPoint.getSignature(), obj.getClass());
    }

    //실제 타겟 객체를 전달받음
    @Before("allMember() && target(obj)")
    public void targetArgs(JoinPoint joinPoint, MemberService obj) {
      log.info("[target]{}, obj={}", joinPoint.getSignature(), obj.getClass());
    }

    //사용된 애너테이션(@ClassAop)를 전달받음
    @Before("allMember() && @target(annotation)")
    public void atTarget(JoinPoint joinPoint, ClassAop annotation) {
      log.info("[@target]{}, obj={}", joinPoint.getSignature(), annotation);
    }

    //사용된 애너테이션(@ClassAop)를 전달받음
    @Before("allMember() && @within(annotation)")
    public void atWithin(JoinPoint joinPoint, ClassAop annotation) {
      log.info("[@within]{}, obj={}", joinPoint.getSignature(), annotation);
    }

    //사용된 애너테이션(@MethodAop)를 전달받음
    @Before("allMember() && @annotation(annotation)")
    public void atWithin(JoinPoint joinPoint, MethodAop annotation) {
      log.info("[@annotation]{}, annotationValue={}", joinPoint.getSignature(), annotation.value());
    }
  }
}
