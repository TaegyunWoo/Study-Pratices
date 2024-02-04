/**
 * author         : 우태균
 * description    : 카프카 프로세서 API 사용
 */
package org.example;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;

import java.util.Properties;

public class SimpleKafkaProcessor {
  private static String APPLICATION_NAME = "processor-application";
  private static String BOOTSTRAP_SERVERS = "localhost:9092";
  private static String STREAM_LOG = "stream_log";
  private static String STREAM_LOG_FILTER = "stream_log_filter";

  public static void main(String[] args) {
    Properties properties = new Properties();
    properties.put(StreamsConfig.APPLICATION_ID_CONFIG, APPLICATION_NAME);
    properties.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
    properties.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
    properties.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());

    //Processor API를 활용한 토폴로지 설정
    Topology topology = new Topology();
    topology.addSource("Source", STREAM_LOG) //소스 토픽 설정
            .addProcessor("Process", () -> new FilterProcessor(), "Source") //프로세서 설정
            .addSink("Sink", STREAM_LOG_FILTER, "Process"); //싱크 토픽 설정

    //스트림즈 앱 생성 및 실행
    KafkaStreams streams = new KafkaStreams(topology, properties);
    streams.start();
  }
}
