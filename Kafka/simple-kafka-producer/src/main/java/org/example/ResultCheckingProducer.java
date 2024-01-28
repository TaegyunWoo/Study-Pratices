/**
 * author         : 우태균
 * description    : 전송 결과를 확인하는 프로듀서
 */
package org.example;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

public class ResultCheckingProducer {
  private final static Logger logger = LoggerFactory.getLogger(SimpleProducer.class);
  private final static String TOPIC_NAME = "test";
  private final static String BOOTSTRAP_SERVERS = "localhost:9092";

  public static void main(String[] args) throws ExecutionException, InterruptedException {
    //Producer 설정
    Properties configs = new Properties();
    configs.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
    configs.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
    configs.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

    //Producer 생성
    KafkaProducer<String, String> producer = new KafkaProducer<>(configs);

    //Record 생성
    String messageValue = "testMessage";
    ProducerRecord<String, String> record = new ProducerRecord<>(TOPIC_NAME, messageValue);

    //동기적으로 결과 처리 (전송 결과가 올때까지 기다림)
    RecordMetadata syncedResult = producer.send(record).get();
    logger.info("syncedResult: {}", syncedResult);

    //비동기적으로 결과 처리 (전송 결과를 기다리지 않고, 전송 결과가 도착했을 때, 관련 로직 실행)
    producer.send(record, new ResultCheckingCallback());

    producer.flush();
    producer.close();
  }
}
