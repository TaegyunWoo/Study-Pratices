/**
 * author         : 우태균
 * description    : 비동기 수동 커밋하고, 그 결과에 따른 작업을 수행하는 컨슈머
 */
package org.example;

import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Arrays;
import java.util.Map;
import java.util.Properties;

/**
 * 이 앱을 실행시킨 후, topic에 produce한 직후 앱 종료. -> 다시 실행했을 때 해당 메시지가 도착한다면, 이전에 커밋이 되지 않았기 때문이라는 것을 알 수 있음.
 */
public class ConsumerWithAdvancedAsyncCommit {
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

      //람다식으로 치환 가능
      consumer.commitAsync(new OffsetCommitCallback() {
        @Override
        public void onComplete(Map<TopicPartition, OffsetAndMetadata> offsets /*커밋된 오프셋 정보*/, Exception exception) {
          if (exception != null) { //커밋 도중 오류가 발생한 경우
            System.err.println("Commit failed!");
            logger.error("Commit failed for offsets {}", offsets, exception);
          } else { //커밋 성공 시
            System.out.println("Commit succeeded!");
          }
        }
      });

      //consumer.commitAsync();
    }
  }
}

