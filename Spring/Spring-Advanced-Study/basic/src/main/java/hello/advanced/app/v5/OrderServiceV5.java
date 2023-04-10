package hello.advanced.app.v5;

import hello.advanced.trace.callback.TraceTemplate;
import hello.advanced.trace.logtrace.LogTrace;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceV5 {
  private final OrderRepositoryV5 orderRepository;
  private final TraceTemplate template;

  //LogTrace를 주입받아, TraceTemplate에 다시 전달
  public OrderServiceV5(OrderRepositoryV5 orderRepository, LogTrace trace) {
    this.orderRepository = orderRepository;
    this.template = new TraceTemplate(trace);
  }

  public void orderItem(String itemId) {
    template.execute("OrderService.orderItem()", () -> {
      orderRepository.save(itemId);
      return null;
    });
  }
}
