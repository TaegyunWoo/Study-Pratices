package reactive.streams;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

//출판자 클래스
public class MyPublisher implements Publisher<String> {
  private Iterator<String> data; //출판자의 데이터

  public MyPublisher() {
    List<String> list = new ArrayList<>();
    for (int i = 0; i < 10; i++) {
      list.add("data[" + i + "]");
    }

    this.data = list.iterator();
  }

  /**
   * 구독자(Subscriber)가 구독할 때 호출할 메서드
   * @param s 구독할 구독자
   */
  @Override
  public void subscribe(Subscriber<? super String> s) {
    System.out.printf("[Publisher] 구독자(%s)로부터 구독이 시작됨.\n", s);

    Subscription subscription = new MySubscription(s, data); //구독정보 생성

    System.out.printf("[Publisher] 구독정보(%s) 생성함.\n", subscription);

    System.out.printf("[Publisher] 생성된 구독정보를 구독자(%s)에게 전달함.\n", s);
    s.onSubscribe(subscription); //구독정보를 구독자에게 전달
  }
}
