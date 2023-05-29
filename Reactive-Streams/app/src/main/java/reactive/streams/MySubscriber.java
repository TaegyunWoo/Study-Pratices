package reactive.streams;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

//구독자 클래스
public class MySubscriber implements Subscriber<String> {
  private static final int PULL_SIZE = 2; //한번에 가져올 데이터의 양
  private int bufferSize = 0; //현재 전달받은 데이터의 개수 관리
  private Subscription subscription; //출판자로부터 전달받은 구독정보 객체

  /**
   * 출판자(Publisher)로부터 구독정보(Subscription)을 전달받을 때 호출되는 메서드
   * @param s 전달받을 구독정보
   */
  @Override
  public void onSubscribe(Subscription s) {
    System.out.printf("[Subscriber] 구독정보(%s) 전달받음.\n", s);
    this.subscription = s;

    this.subscription.request(PULL_SIZE); //PULL_SIZE 만큼의 데이터 요청
  }

  /**
   * 구독정보(Subscription)로부터 데이터를 전달받을 때 호출되는 메서드
   * @param s 전달받은 데이터
   */
  @Override
  public void onNext(String s) {
    System.out.printf("[Subscriber] 데이터(%s)를 전달받음\n", s);
    bufferSize++; //하나를 전달받았기 때문에, 버퍼 사이즈 1 증가

    if (bufferSize == PULL_SIZE) {
      bufferSize = 0; //버퍼 사이즈 초기화
      this.subscription.request(PULL_SIZE);
    }
  }

  /**
   * 구독정보(Subscription)로부터 데이터 전달받는 도중, 에러가 발생하면 호출되는 메서드
   * @param t the throwable signaled
   */
  @Override
  public void onError(Throwable t) {

  }

  /**
   * 구독정보(Subscription)로부터 모든 데이터를 전달받으면 호출되는 메서드
   * 이때 모든 데이터는 한번에 전달받을 n개의 데이터가 아닌, 출판자가 가지고 있는 전체 데이터를 의미
   */
  @Override
  public void onComplete() {
    System.out.println("[Subscriber] 모든 데이터를 다 읽었기 때문에, 구독이 종료됨.");
  }
}