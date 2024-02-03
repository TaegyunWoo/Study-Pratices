/**
 * author         : 우태균
 * description    : 조인을 수행하는 Streams 앱
 */
package org.example;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;

import java.util.Properties;

public class JoinKafkaStreams {
  private static final String APPLICATION_NAME = "streams-app";
  private static final String BOOTSTRAP_SERVERS = "localhost:9092";
  private static final String ADDRESS_TOPIC = "address";
  private static final String ORDER_TOPIC = "order";
  private static final String ORDER_JOIN_TOPIC = "address_order_join";

  public static void main(String[] args) {
    //스트림즈 앱 설정
    Properties properties = new Properties();
    properties.put(StreamsConfig.APPLICATION_ID_CONFIG, APPLICATION_NAME);
    properties.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
    properties.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
    properties.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());

    //스트림즈 토폴로지 정의 객체
    StreamsBuilder streamsBuilder = new StreamsBuilder();
    KTable<String, String> addressTable = streamsBuilder.table(ADDRESS_TOPIC); //KTable 형태의 소스 프로세서
    KStream<String, String> orderStream = streamsBuilder.stream(ORDER_TOPIC); //KStream 형태의 소스 프로세서

    orderStream
        .join(addressTable, (order, address) -> order + " send to " + address) //조인
        .to(ORDER_JOIN_TOPIC);

    //스트림즈 앱 생성 및 실행
    KafkaStreams streams = new KafkaStreams(streamsBuilder.build(), properties); //스트림즈 앱 생성
    streams.start(); //스트림즈 앱 실행
  }
}
