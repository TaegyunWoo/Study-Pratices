package hello.advanced.trace.logtrace;

import hello.advanced.trace.TraceStatus;
import org.junit.jupiter.api.Test;

class ThreadLocalLogTraceTest {
  ThreadLocalLogTrace trace = new ThreadLocalLogTrace();

  @Test
  void begin_end_level2() {
    TraceStatus status1 = trace.begin("hello1"); //depth=1
    TraceStatus status2 = trace.begin("hello2"); //depth=2
    trace.end(status2); //depth=1 <- depth=2
    trace.end(status1); //depth=0 <- depth=1
  }

  @Test
  void begin_exception_level2() {
    TraceStatus status1 = trace.begin("hello1"); //depth=1
    TraceStatus status2 = trace.begin("hello2"); //depth=2
    trace.exception(status2, new IllegalStateException()); //depth=1 <- depth=2 (exception)
    trace.exception(status1, new IllegalStateException()); //depth=0 <- depth=1 (exception)
  }
}