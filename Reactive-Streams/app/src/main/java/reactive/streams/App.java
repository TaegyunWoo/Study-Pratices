package reactive.streams;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;

public class App {

    public static void main(String[] args) {
        Publisher<String> publisher = new MyPublisher(); //출판자 생성
        Subscriber<String> subscriber = new MySubscriber(); //구독자 생성

        publisher.subscribe(subscriber); //구독 요청

    }
}
