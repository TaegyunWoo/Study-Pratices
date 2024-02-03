/**
 * author         : 우태균
 * description    : KStreamr과 GlobalKTable 조인
 */
package org.example;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.GlobalKTable;
import org.apache.kafka.streams.kstream.KStream;

import java.util.Properties;

public class KStreamJoinGlobalKTable {
  private static final String APPLICATION_NAME = "streams-app";
  private static final String BOOTSTRAP_SERVERS = "localhost:9092";
  private static final String ADDRESS_GLOBAL_TABLE = "address_v2";
  private static final String ORDER_STREAM = "order";
  private static final String ORDER_JOIN_STREAM = "address_order_join";

  public static void main(String[] args) {
    //스트림즈 앱 설정
    Properties properties = new Properties();
    properties.put(StreamsConfig.APPLICATION_ID_CONFIG, APPLICATION_NAME);
    properties.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
    properties.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
    properties.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());

    //스트림즈 토폴로지 정의 객체
    StreamsBuilder streamsBuilder = new StreamsBuilder();
    GlobalKTable<String, String> addressGlobalKTable = streamsBuilder.globalTable(ADDRESS_GLOBAL_TABLE);
    KStream<String, String> orderStream = streamsBuilder.stream(ORDER_STREAM);

    //스트림즈 앱 생성 및 실행
    orderStream.join(addressGlobalKTable,
        (orderKey, orderValue) -> orderValue, //어떤 값을 globalKTable의 키와 매핑할 것인지 선택
        (order, address) -> order + " send to " + address)
        .to(ORDER_JOIN_STREAM);
    KafkaStreams streams = new KafkaStreams(streamsBuilder.build(), properties);
    streams.start(); //스트림즈 앱 실행
  }
}
