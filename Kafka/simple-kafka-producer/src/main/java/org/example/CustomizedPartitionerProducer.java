/**
 * author         : 우태균
 * description    : 커스텀 파티셔너를 적용한 프로듀서
 */
package org.example;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class CustomizedPartitionerProducer {
  private final static Logger logger = LoggerFactory.getLogger(SimpleProducer.class);
  private final static String TOPIC_NAME = "test";
  private final static String BOOTSTRAP_SERVERS = "localhost:9092";

  public static void main(String[] args) {
    //Producer 설정
    Properties configs = new Properties();
    configs.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
    configs.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
    configs.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
    configs.put(ProducerConfig.PARTITIONER_CLASS_CONFIG, CustomPartitioner.class); //커스텀 파티셔너 적용

    //Producer 생성
    KafkaProducer<String, String> producer = new KafkaProducer<>(configs);

    //Record 생성
    String messageValue = "testMessage";
    ProducerRecord<String, String> recordToPartition0 = new ProducerRecord<>(TOPIC_NAME, "Pangyo", messageValue);
    ProducerRecord<String, String> recordToRandomPartition = new ProducerRecord<>(TOPIC_NAME, "Seoul", messageValue);

    //Record 전송
    producer.send(recordToPartition0);
    producer.send(recordToRandomPartition);
    logger.info("{}", recordToPartition0);
    logger.info("{}", recordToRandomPartition);
    producer.flush();
    producer.close();
  }
}
