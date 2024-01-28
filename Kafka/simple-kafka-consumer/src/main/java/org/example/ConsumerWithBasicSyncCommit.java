/**
 * author         : 우태균
 * description    : 기본적인 동기 수동 커밋 로직이 포함된 컨슈머
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

/**
 * 이 앱을 실행시킨 후, topic에 produce한 직후 앱 종료. -> 다시 실행했을 때 해당 메시지가 도착한다면, 이전에 커밋이 되지 않았기 때문이라는 것을 알 수 있음.
 */
public class ConsumerWithBasicSyncCommit {
  private final static Logger logger = LoggerFactory.getLogger(ConsumerWithBasicSyncCommit.class);
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
    configs.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false); //자동 커밋 off

    //컨슈머 생성 및 구독
    KafkaConsumer<String, String> consumer = new KafkaConsumer<>(configs);
    consumer.subscribe(Arrays.asList(TOPIC_NAME));

    //데이터 가져오기
    while (true) {
      ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(1));
      for (ConsumerRecord record : records) {
        logger.info("{}", record);
      }

      //가져온 레코드들에 대한 모든 작업(비즈니스 로직)을 끝낸 후, 해당 레코드들에 대해 커밋 (commitSync 메서드: 동기적으로 동작하는 커밋. 따라서 커밋이 완료될 때까지 대기하게 됨.)
      //(커밋: 특정 오프셋까지에 대한 처리를 모두 마무리했다고 알림. 따라서, 같은 컨슈머 그룹의 컨슈머들은 해당 오프셋 이후의 데이터부터 받게 되어, 중복 처리를 방지함)
      consumer.commitSync(); //기본적으로 마지막에 읽은 레코드까지 커밋
    }
  }
}
