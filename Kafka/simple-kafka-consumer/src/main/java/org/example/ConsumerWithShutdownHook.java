/**
 * author         : 우태균
 * description    : Shutdown hook을 활용해, 안전하게 자신의 프로세스를 종료시키는 컨슈머
 */
package org.example;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

// kill {ConsumerWithShutdownHook PID} 로 확인 가능 (PID 확인: ps -ef | grep ConsumerWithShutdownHook)
public class ConsumerWithShutdownHook {
  private final static Logger logger = LoggerFactory.getLogger(ConsumerWithBasicSyncCommit.class);
  private final static String TOPIC_NAME = "test";
  private final static String BOOTSTRAP_SERVERS = "localhost:9092";
  private final static String GROUP_ID = "test-group"; //컨슈머 그룹
  private static KafkaConsumer<String, String> consumer;

  public static void main(String[] args) {
    //컨슈머 설정
    Properties configs = new Properties();
    configs.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
    configs.put(ConsumerConfig.GROUP_ID_CONFIG, GROUP_ID);
    configs.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
    configs.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());

    //컨슈머 생성 및 구독
    consumer = new KafkaConsumer<>(configs);
    consumer.subscribe(Arrays.asList(TOPIC_NAME));

    //Shutdown hook 등록
    Runtime.getRuntime().addShutdownHook(new ShutdownThread());

    //try문을 활용하여, 안전한 컨슈머 종료
    try {
      //데이터 가져오기
      while (true) {
        //poll 메서드는 컨슈머의 wakeup 호출 이후 실행되면, WakeupException 발생
        ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(1));

        for (ConsumerRecord record : records) {
          logger.info("{}", record);
        }

      } //while문 종료

    } catch (WakeupException e) {
      //WakeupException : 컨슈머의 wakeup 호출 후, poll을 호출하면 WakeupException 발생
      logger.warn("Wakeup consumer!");
      //리소스 종료 처리

    } finally {
      consumer.close(); //마지막에 명시적으로 컨슈머를 종료함.
    }

  }

  //OS 등에 의해 종료 명령이 발생하면 실행되는 쓰레드
  private static class ShutdownThread extends Thread {
    @Override
    public void run() {
      logger.info("Shutdown hook");
      consumer.wakeup(); //해당 컨슈머의 wakeup 메서드 호출 (이후 polling시, WakeupException 유도)
    }
  }
}
