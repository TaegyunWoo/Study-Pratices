/**
 * author         : 우태균
 * description    : 프로듀서 예시
 */
package org.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

public class SimpleProducer {
  private final static Logger logger = LoggerFactory.getLogger(SimpleProducer.class);
  private final static String TOPIC_NAME = "test";
  private final static String BOOTSTRAP_SERVERS = "localhost:9092";

  public static void main(String[] args) {
    //Producer 설정
    Properties configs = new Properties();
    configs.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
    configs.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
    configs.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

    //Producer 생성
    KafkaProducer<String, String> producer = new KafkaProducer<>(configs);

    //Record 생성 - 키값이 없는 Record
    String messageValue = "testMessage";
    ProducerRecord<String, String> recordWithoutKey = new ProducerRecord<>(TOPIC_NAME, messageValue);

    //Record 생성 - 키값이 있는 Record
    String messageKey = "testKey";
    ProducerRecord<String, String> recordWithKey = new ProducerRecord<>(TOPIC_NAME, messageKey, messageValue);

    //Record 생성 - 저장할 파티션 직접 지정
    int partitionNum = 0;
    ProducerRecord<String, String> recordWithPartitionNum = new ProducerRecord<>(TOPIC_NAME, partitionNum, messageKey, messageValue);

    //그 외의 다양한 Record 생성 방법 존재...

    //Record 전송
    producer.send(recordWithoutKey); //아직 전달하지는 않고, 파티션마다의 배치 데이터로 적재됨
    producer.send(recordWithKey); //아직 전달하지는 않고, 파티션마다의 배치 데이터로 적재됨
    producer.send(recordWithPartitionNum); //아직 전달하지는 않고, 파티션마다의 배치 데이터로 적재됨
    logger.info("{}", recordWithKey);
    producer.flush(); //이제 적재된 모든 Record를 배치로 묶어서 전송
    producer.close(); //안전하게 Producer 종료
  }
}

