package hello.advanced.trace.threadlocal;

import hello.advanced.trace.threadlocal.code.FieldService;
import hello.advanced.trace.threadlocal.code.ThreadLocalService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class ThreadLocalServiceTest {

  private ThreadLocalService threadLocalService = new ThreadLocalService();

  @Test
  void threadLocal() {
    log.info("main start");

    //쓰레드에서 수행할 로직 A
    Runnable userA = () -> {
      threadLocalService.logic("userA");
    };

    //쓰레드에서 수행할 로직 B
    Runnable userB = () -> {
      threadLocalService.logic("userB");
    };

    //쓰레드 A 생성
    Thread threadA = new Thread(userA);
    threadA.setName("thread-A");

    //쓰레드 B 생성
    Thread threadB = new Thread(userB);
    threadB.setName("thread-B");

    threadA.start(); //쓰레드 A 실행
//    sleep(2000); //동시성 문제 발생 X (메인쓰레드가 2초 대기)
     sleep(100); //동시성 문제 발생 O (메인쓰레드가 0.1초 대기)
    threadB.start(); //쓰레드 B 실행 (메인쓰레드가 대기한 후, 호출되는 코드)

    sleep(3000); //쓰레드 B가 끝날때까지, 메인 쓰레드가 대기
    log.info("main exit");
  }

  private void sleep(long millis) {
    try {
      Thread.sleep(millis);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
