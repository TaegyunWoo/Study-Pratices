/**
 * author         : 우태균
 * description    : 파티션을 직접 할당받는 컨슈머
 */
package org.example;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;
import java.util.Set;

public class ConsumerWithExactPartition {
  private final static Logger logger = LoggerFactory.getLogger(ConsumerWithBasicSyncCommit.class);
  private final static String TOPIC_NAME = "test";
  private final static String BOOTSTRAP_SERVERS = "localhost:9092";
  private final static String GROUP_ID = "test-group"; //컨슈머 그룹
  private final static int PARTITION_NUM = 0;

  public static void main(String[] args) {
    //컨슈머 설정
    Properties configs = new Properties();
    configs.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
    configs.put(ConsumerConfig.GROUP_ID_CONFIG, GROUP_ID);
    configs.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
    configs.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());

    //컨슈머 생성 및 구독
    KafkaConsumer<String, String> consumer = new KafkaConsumer<>(configs);
//    consumer.subscribe(Arrays.asList(TOPIC_NAME)); //기존 구독 방법 (이땐, 파티션 번호가 자동으로 부여됨)
    consumer.assign(Collections.singleton(new TopicPartition(TOPIC_NAME, PARTITION_NUM))); //특정 토픽의 파티션 번호를 직접 할당
    Set<TopicPartition> assignmentInfo = consumer.assignment(); //구독 정보 (할당된 토픽, 파티션 정보)

    //데이터 가져오기
    while (true) {
      ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(1));
      for (ConsumerRecord record : records) {
        logger.info("{}", record);
      }
    }
  }
}
