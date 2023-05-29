package reactive.streams;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.Iterator;

//구독 정보 클래스 (구독자, 구독할 데이터 정보)
public class MySubscription implements Subscription {
  private Subscriber subscriber;
  private Iterator<String> data;

  public MySubscription(Subscriber subscriber, Iterator<String> data) {
    this.subscriber = subscriber;
    this.data = data;
  }

  /**
   * 구독자(Subscriber)가 데이터를 요청할 때, 호출되는 메서드
   * @param n 한번에 요청할 데이터의 양
   */
  @Override
  public void request(long n) {
    System.out.printf("[Subscription] Subscriber가 %d개의 데이터를 요청함.\n", n);
    while (n-- > 0) { //n번 반복
      if (data.hasNext()) { //만약 데이터가 더 존재한다면
        subscriber.onNext(data.next());

      } else { //만약 데이터가 더 존재하지 않는다면
        subscriber.onComplete();
        break;
      }
    }
  }

  /**
   * 구독자(Subscriber)가 데이터 전송을 중단시킬 때, 호출되는 메서드
   */
  @Override
  public void cancel() {

  }
}
