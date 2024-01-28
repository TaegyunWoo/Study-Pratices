/**
 * author         : 우태균
 * description    : 리밸런스 리스너를 사용하여, 카프카 클러스터에서 리밸런스 진행시 현재까지의 레코드 오프셋을 커밋해두고, 중복 처리가 발생하지 않도록 처리된 컨슈머
 *                  리밸런스 발동 조건 : 동일한 토픽에서, 새로운 컨슈머 등록 or 기존 컨슈머 종료
 */
package org.example;

import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.*;

public class ConsumerWithRebalanceListener {
  private final static Logger logger = LoggerFactory.getLogger(ConsumerWithBasicSyncCommit.class);
  private final static String TOPIC_NAME = "test";
  private final static String BOOTSTRAP_SERVERS = "localhost:9092";
  private final static String GROUP_ID = "test-group"; //컨슈머 그룹

  private static KafkaConsumer<String, String> consumer;
  private static Map<TopicPartition, OffsetAndMetadata> currentOffset;

  public static void main(String[] args) {
    //컨슈머 설정
    Properties configs = new Properties();
    configs.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
    configs.put(ConsumerConfig.GROUP_ID_CONFIG, GROUP_ID);
    configs.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
    configs.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
    configs.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false); //자동 커밋 off

    //컨슈머 생성 및 구독
    consumer = new KafkaConsumer<>(configs);
    consumer.subscribe(Arrays.asList(TOPIC_NAME), new RebalanceListener()); //토픽 구독 및 리밸런스 리스너 등록

    //데이터 가져오기
    while (true) {
      ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(1));
      currentOffset = new HashMap<>(); //오프셋 커밋 정보

      for (ConsumerRecord record : records) {
        logger.info("{}", record);

        //현재 오프셋 정보 put
        currentOffset.put(
            new TopicPartition(record.topic(), record.partition()), //레코드의 토픽, 파티션 정보를 기반으로
            new OffsetAndMetadata(record.offset() + 1, null) //현재 레코드의 오프셋 + 1로 커밋해야함. (커밋된 오프셋 번호부터 다시 전송하기 때문에. 1을 더하지 않는다면, 중복처리 발생)
        );

        consumer.commitSync(); //일반적인 커밋
      }
    }
  }

  private static class RebalanceListener implements ConsumerRebalanceListener {

    //카프카 클러스터에서 리밸런스가 시작되기 직전에 호출되는 메서드
    @Override
    public void onPartitionsRevoked(Collection<TopicPartition> partitions) {
      logger.warn("파티션들이 다시 할당될 예정입니다.");
      consumer.commitSync(currentOffset); //현재까지 처리된 오프셋 커밋 -> 리밸런스 이후에 중복 처리되는 것을 방지하기 위해, 현재까지 처리한 레코드의 오프셋 커밋
    }

    //카프카 클러스터에서 리밸런스가 끝난 뒤, 파티션이 할당 완료되면 호출
    @Override
    public void onPartitionsAssigned(Collection<TopicPartition> partitions) {
      logger.warn("파티션들이 다시 할당되었습니다.");
    }
  }
}
