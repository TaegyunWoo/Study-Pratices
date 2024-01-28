/**
 * author         : 우태균
 * description    : 기본 컨슈머
 */
package org.example;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

public class SimpleConsumer {
  private final static Logger logger = LoggerFactory.getLogger(SimpleConsumer.class);
  private final static String TOPIC_NAME = "test";
  private final static String BOOTSTRAP_SERVERS = "localhost:9092";
  private final static String GROUP_ID = "test-group"; //컨슈머 그룹

  public static void main(String[] args) {
    //컨슈머 설정
    Properties configs = new Properties();
    configs.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
    configs.put(ConsumerConfig.GROUP_ID_CONFIG, GROUP_ID);
    configs.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
    configs.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());

    //컨슈머 생성 및 구독
    KafkaConsumer<String, String> consumer = new KafkaConsumer<>(configs);
    consumer.subscribe(Arrays.asList(TOPIC_NAME));

    //데이터 가져오기
    while (true) {
      ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(1)); //1초마다 데이터 가져옴 (Fetcher에 의해 미리 내부큐에 적재된 데이터를 1초마다 가져옴)
      for (ConsumerRecord record : records) {
        logger.info("{}", record);
      }
    }
  }
}
