package my.study.webflux.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Stream;

@RestController
public class BasicController {

  /*
   * Flux(Publisher)가 데이터("Hello",  " World", "!")를 방출(emit)하도록 함
   * (Flux는 Reactive Streams의 Publisher를 구현한 N 요소의 스트림을 표현하는 Reactor 클래스)
   * 요청 : curl -i localhost:8080/controller -H 'Accept: application/stream+json'
   */
  @GetMapping("/controller")
  public Flux<String> hello() {
    return Flux.just("Hello",  " World", "!");
  }

  /*
   * 무한 스트림을 만들고, limit을 걸어서 반복 횟수를 제한
   * 요청 : curl -i localhost:8080/controller/limited-stream -H 'Accept: application/stream+json'
   */
  @GetMapping("/controller/limited-stream")
  public Flux<Map<String, Integer>> limitedStream() {
    //무한 스트림 (item을 1씩 증가시킴)
    Stream<Integer> stream = Stream.iterate(0, item -> item + 1);
    return Flux.fromStream(stream.limit(100)) //limit 100
        .map(i -> Collections.singletonMap("value", i));
  }

  /*
   * 무한 스트림 반환
   * 요청 : curl -i localhost:8080/controller/infinite-stream -H 'Accept: application/stream+json'
   */
  @GetMapping("/controller/infinite-stream")
  public Flux<Map<String, Integer>> infiniteStream() {
    //무한 스트림 (item을 1씩 증가시킴)
    Stream<Integer> stream = Stream.iterate(0, item -> item + 1);
    return Flux.fromStream(stream)
        .delayElements(Duration.ofSeconds(1)) //요소마다 1초씩 대기 (즉 1초마다 emit)
        .map(i -> Collections.singletonMap("value", i));
  }

  /*
   * 단일 요소를 Mono로 받아, 대문자로 수정해서 Mono로 단일 요소 emit
   * 요청 : curl -i localhost:8080/mono-echo -H 'Accept: application/stream+json'
   */
  @PostMapping("/controller/mono-echo")
  public Mono<String> monoEcho(@RequestBody Mono<String> body) {
    return body.map(String::toUpperCase); //대문자로 변환해서 Mono로 반환
  }
}
