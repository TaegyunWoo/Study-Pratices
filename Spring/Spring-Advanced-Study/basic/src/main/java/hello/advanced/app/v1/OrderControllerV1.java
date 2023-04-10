package hello.advanced.app.v1;

import hello.advanced.trace.TraceStatus;
import hello.advanced.trace.hellotrace.HelloTraceV1;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OrderControllerV1 {

  private final OrderServiceV1 orderService;
  private final HelloTraceV1 trace;

  @GetMapping("/v1/request")
  public String request(String itemId) {
    TraceStatus status = null;
    try {
      status = trace.begin("OrderController.request()");
      orderService.orderItem(itemId);
      trace.end(status);
      return "ok";
    } catch (Exception e) {
      trace.exception(status, e);
      throw e; //반드시 예외를 다시 던져주어야 한다. (로깅 로직이 원래 로직에 영향을 주면 안되므로)
    }
  }
}
