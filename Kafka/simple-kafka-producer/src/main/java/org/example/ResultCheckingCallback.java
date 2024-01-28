/**
 * author         : 우태균
 * description    : 전송 결과를 비동기적으로 확인할 때, 사용되는 클래스
 */
package org.example;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ResultCheckingCallback implements Callback {
  private final static Logger logger = LoggerFactory.getLogger(ResultCheckingCallback.class);

  @Override
  public void onCompletion(RecordMetadata metadata, Exception exception) {
    if (exception != null) { //오류 결과가 응답되었을 때
      logger.error(exception.getMessage(), exception);
    } else { //정상 결과가 응답되었을 때
      logger.info("succeed result: {}", metadata.toString());
    }
  }
}
