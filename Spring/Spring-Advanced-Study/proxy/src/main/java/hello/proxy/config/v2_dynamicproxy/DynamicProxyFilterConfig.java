package hello.proxy.config.v2_dynamicproxy;

import hello.proxy.app.v1.*;
import hello.proxy.config.v2_dynamicproxy.handler.LogTraceBasicHandler;
import hello.proxy.config.v2_dynamicproxy.handler.LogTraceFilterHandler;
import hello.proxy.trace.logtrace.LogTrace;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Proxy;

@Configuration
public class DynamicProxyFilterConfig {

  public static final String[] PATTERNS = {"request*", "order*", "save*"};

  @Bean
  public OrderControllerV1 orderControllerV1(LogTrace logTrace) {
    //target
    OrderControllerV1 controller = new OrderControllerV1Impl(orderServiceV1(logTrace));

    //proxy
    OrderControllerV1 proxy = (OrderControllerV1) Proxy.newProxyInstance(
        OrderControllerV1.class.getClassLoader(),
        new Class[] {OrderControllerV1.class},
        new LogTraceFilterHandler(controller, logTrace, PATTERNS)
    );

    return proxy;
  }

  @Bean
  public OrderServiceV1 orderServiceV1(LogTrace logTrace) {
    //target
    OrderServiceV1 service = new OrderServiceV1Impl(orderRepositoryV1(logTrace));

    //proxy
    OrderServiceV1 proxy = (OrderServiceV1) Proxy.newProxyInstance(
        OrderServiceV1.class.getClassLoader(),
        new Class[] {OrderServiceV1.class},
        new LogTraceFilterHandler(service, logTrace, PATTERNS)
    );

    return proxy;
  }

  @Bean
  public OrderRepositoryV1 orderRepositoryV1(LogTrace logTrace) {
    //target
    OrderRepositoryV1 repository = new OrderRepositoryV1Impl();

    //proxy
    OrderRepositoryV1 proxy = (OrderRepositoryV1) Proxy.newProxyInstance(
        OrderRepositoryV1.class.getClassLoader(),
        new Class[] {OrderRepositoryV1.class},
        new LogTraceFilterHandler(repository, logTrace, PATTERNS)
    );

    return proxy;
  }
}
