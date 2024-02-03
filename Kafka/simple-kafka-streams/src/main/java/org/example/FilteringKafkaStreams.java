/**
 * author         : 우태균
 * description    : 필터링하는 스트림 프로세서가 추가된 스트림즈 애플리케이션
 */
package org.example;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;

import java.util.Properties;

public class FilteringKafkaStreams {
  private static final String APPLICATION_NAME = "streams-app";
  private static final String BOOTSTRAP_SERVERS = "localhost:9092";
  private static final String STREAM_LOG_TOPIC = "stream_log";
  private static final String STREAM_LOG_FILTERED_TOPIC = "stream_log_filter";

  public static void main(String[] args) {
    //스트림즈 앱 설정
    Properties properties = new Properties();
    properties.put(StreamsConfig.APPLICATION_ID_CONFIG, APPLICATION_NAME);
    properties.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
    properties.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
    properties.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());

    //스트림즈 토폴로지 정의 객체
    StreamsBuilder streamsBuilder = new StreamsBuilder();
    KStream<String, String> streamLogKStream = streamsBuilder.stream(STREAM_LOG_TOPIC); //STREAM_LOG 토픽을 기반으로 K스트림 생성한다고 정의

    //stream processor (Filtering)
    streamLogKStream = streamLogKStream.filter((key, value) -> value.length() > 5); //value 길이가 5 이상인 경우에만 통과

    streamLogKStream.to(STREAM_LOG_FILTERED_TOPIC); //생성한 K스트림의 레코드들을 STREAM_LOG_FILTERED_TOPIC 토픽으로 전달한다고 정의

    //혹은 아래 같이 간소화 가능
//    streamLogKStream
//        .filter((key, value) -> value.length() > 5)
//        .to(STREAM_LOG_FILTERED_TOPIC);

    //스트림즈 앱 생성 및 실행
    KafkaStreams streams = new KafkaStreams(streamsBuilder.build(), properties); //스트림즈 앱 생성
    streams.start(); //스트림즈 앱 실행
  }
}
