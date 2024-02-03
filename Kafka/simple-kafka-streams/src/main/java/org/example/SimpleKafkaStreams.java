/**
 * author         : 우태균
 * description    : 스트림 프로세서는 제외되고, 소스/싱크 프로세서로만 구성된 스트림즈.
 *                  stream_log 토픽의 레코드들을 stream_log_copy 토픽으로 전달하는 스트림즈 앱.
 */
package org.example;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;

import java.util.Properties;

public class SimpleKafkaStreams {
  private static final String APPLICATION_NAME = "streams-app";
  private static final String BOOTSTRAP_SERVERS = "localhost:9092";
  private static final String STREAM_LOG_TOPIC = "stream_log";
  private static final String STREAM_LOG_COPY_TOPIC = "stream_log_copy";

  public static void main(String[] args) {
    //스트림즈 앱 설정
    Properties properties = new Properties();
    properties.put(StreamsConfig.APPLICATION_ID_CONFIG, APPLICATION_NAME);
    properties.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
    properties.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
    properties.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());

    //스트림즈 토폴로지 정의 객체
    StreamsBuilder streamsBuilder = new StreamsBuilder();
    KStream<Object, Object> streamLogKStream = streamsBuilder.stream(STREAM_LOG_TOPIC); //STREAM_LOG 토픽을 기반으로 K스트림 생성한다고 정의
    streamLogKStream.to(STREAM_LOG_COPY_TOPIC); //생성한 K스트림의 레코드들을 STREAM_LOG_COPY 토픽으로 전달한다고 정의

    //스트림즈 앱 생성 및 실행
    KafkaStreams streams = new KafkaStreams(streamsBuilder.build(), properties); //스트림즈 앱 생성
    streams.start(); //스트림즈 앱 실행
  }
}
