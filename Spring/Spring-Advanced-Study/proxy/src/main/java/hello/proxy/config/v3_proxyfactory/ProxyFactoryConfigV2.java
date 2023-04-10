package hello.proxy.config.v3_proxyfactory;

import hello.proxy.app.v2.OrderControllerV2;
import hello.proxy.app.v2.OrderRepositoryV2;
import hello.proxy.app.v2.OrderServiceV2;
import hello.proxy.config.v3_proxyfactory.advice.LogTraceAdvice;
import hello.proxy.trace.logtrace.LogTrace;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.Advisor;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class ProxyFactoryConfigV2 {

  @Bean
  public OrderControllerV2 orderControllerV1(LogTrace logTrace) {
    OrderControllerV2 controller = new OrderControllerV2(orderServiceV2(logTrace));

    ProxyFactory proxyFactory = new ProxyFactory(controller);
    proxyFactory.addAdvisor(getAdvisor(logTrace));

    OrderControllerV2 proxy = (OrderControllerV2) proxyFactory.getProxy();

    log.info("ProxyFactory proxy={}, target={}", proxy.getClass(), controller.getClass());

    return proxy;
  }

  @Bean
  public OrderServiceV2 orderServiceV2(LogTrace logTrace) {
    OrderServiceV2 service = new OrderServiceV2(orderRepositoryV2(logTrace));

    ProxyFactory proxyFactory = new ProxyFactory(service);
    proxyFactory.addAdvisor(getAdvisor(logTrace));

    OrderServiceV2 proxy = (OrderServiceV2) proxyFactory.getProxy();

    log.info("ProxyFactory proxy={}, target={}", proxy.getClass(), service.getClass());

    return proxy;
  }

  @Bean
  public OrderRepositoryV2 orderRepositoryV2(LogTrace logTrace) {
    OrderRepositoryV2 repository = new OrderRepositoryV2();

    ProxyFactory proxyFactory = new ProxyFactory(repository);
    proxyFactory.addAdvisor(getAdvisor(logTrace));

    OrderRepositoryV2 proxy = (OrderRepositoryV2) proxyFactory.getProxy();

    log.info("ProxyFactory proxy={}, target={}", proxy.getClass(), repository.getClass());

    return proxy;
  }

  private Advisor getAdvisor(LogTrace logTrace) {
    //pointcut
    NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut();
    pointcut.setMappedNames("request*", "order*", "save*");

    //advice
    LogTraceAdvice advice = new LogTraceAdvice(logTrace);

    //pointcut + advice = advisor
    return new DefaultPointcutAdvisor(pointcut, advice);
  }
}
