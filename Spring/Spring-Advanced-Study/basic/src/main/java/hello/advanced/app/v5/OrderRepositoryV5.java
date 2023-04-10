package hello.advanced.app.v5;

import hello.advanced.trace.callback.TraceCallback;
import hello.advanced.trace.callback.TraceTemplate;
import hello.advanced.trace.logtrace.LogTrace;
import org.springframework.stereotype.Repository;

@Repository
public class OrderRepositoryV5 {

  private final TraceTemplate template;

  //LogTrace를 주입받아, TraceTemplate에 다시 전달
  public OrderRepositoryV5(LogTrace trace) {
    this.template = new TraceTemplate(trace);
  }

  public void save(String itemId) {

    template.execute("OrderRepository.save()", (TraceCallback<Void>) () -> {
      if (itemId.equals("ex")) {
        throw new IllegalStateException("예외 발생!");
      }
      sleep(1000);
      return null;
    });
  }

  private void sleep(int millis) {
    try {
      Thread.sleep(millis);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
