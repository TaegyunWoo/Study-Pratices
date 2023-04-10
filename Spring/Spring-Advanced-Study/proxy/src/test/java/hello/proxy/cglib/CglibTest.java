package hello.proxy.cglib;

import hello.proxy.cglib.code.TimeMethodInterceptor;
import hello.proxy.common.service.ConcreteService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.cglib.proxy.Enhancer;

@Slf4j
public class CglibTest {
  @Test
  void cglib() {
    ConcreteService target = new ConcreteService();

    Enhancer enhancer = new Enhancer();
    enhancer.setSuperclass(ConcreteService.class); //proxy 클래스의 부모클래스 지정 (구체 클래스를 상속받아도 프록시 역할을 할 수 있으므로)
    enhancer.setCallback(new TimeMethodInterceptor(target)); //proxy 에서 동작할 로직 전달

    ConcreteService proxy = (ConcreteService) enhancer.create(); //proxy 객체 생성
    log.info("targetClass = {}", target.getClass());
    log.info("proxyClass = {}", proxy.getClass());

    proxy.call();
  }
}
