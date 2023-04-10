package hello.proxy.config.v3_proxyfactory;

import hello.proxy.app.v1.*;
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
public class ProxyFactoryConfigV1 {

  @Bean
  public OrderControllerV1 orderControllerV1(LogTrace logTrace) {
    OrderControllerV1 controller = new OrderControllerV1Impl(orderServiceV1(logTrace));

    ProxyFactory proxyFactory = new ProxyFactory(controller);
    proxyFactory.addAdvisor(getAdvisor(logTrace));

    OrderControllerV1 proxy = (OrderControllerV1) proxyFactory.getProxy();

    log.info("ProxyFactory proxy={}, target={}", proxy.getClass(), controller.getClass());

    return proxy;
  }

  @Bean
  public OrderServiceV1 orderServiceV1(LogTrace logTrace) {
    OrderServiceV1 service = new OrderServiceV1Impl(orderRepositoryV1(logTrace));

    ProxyFactory proxyFactory = new ProxyFactory(service);
    proxyFactory.addAdvisor(getAdvisor(logTrace));

    OrderServiceV1 proxy = (OrderServiceV1) proxyFactory.getProxy();

    log.info("ProxyFactory proxy={}, target={}", proxy.getClass(), service.getClass());

    return proxy;
  }

  @Bean
  public OrderRepositoryV1 orderRepositoryV1(LogTrace logTrace) {
    OrderRepositoryV1 repository = new OrderRepositoryV1Impl();

    ProxyFactory proxyFactory = new ProxyFactory(repository);
    proxyFactory.addAdvisor(getAdvisor(logTrace));

    OrderRepositoryV1 proxy = (OrderRepositoryV1) proxyFactory.getProxy();

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
