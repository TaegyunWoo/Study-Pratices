/**
 * author         : 우태균
 * description    : 싱크 커넥터가 수행해야 하는 작업 테스크
 */
package org.example;

import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.connect.errors.ConnectException;
import org.apache.kafka.connect.sink.SinkRecord;
import org.apache.kafka.connect.sink.SinkTask;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;

public class SingleFileSinkTask extends SinkTask {
  private SingleFileSinkConnectorConfig config;
  private File file;
  private FileWriter fileWriter;

  //테스크 버전 (보통 커넥터와 동일한 버전으로 지정)
  @Override
  public String version() {
    return "1.0";
  }

  //커넥터를 실행할 때 설정한 옵션을 토대로 리소스를 초기화
  @Override
  public void start(Map<String, String> props) {
    try {
      config = new SingleFileSinkConnectorConfig(props);
      file = new File(config.getString(config.DIR_FILE_NAME));
      fileWriter = new FileWriter(file, true);
    } catch (Exception e) {
      throw new ConnectException(e.getMessage(), e);
    }
  }

  //가져온 데이터를 처리하는 메서드
  @Override
  public void put(Collection<SinkRecord> records) {
    try {
      for (SinkRecord record : records) {
        fileWriter.write(record.value().toString() + "\n");
      }
    } catch (IOException e) {
      throw new ConnectException(e.getMessage(), e);
    }
  }

  //데이터를 실제로 저장할 때 사용하는 메서드 (일정 주기마다)
  @Override
  public void flush(Map<TopicPartition, OffsetAndMetadata> currentOffsets) {
    try {
      fileWriter.flush();
    } catch (IOException e) {
      throw new ConnectException(e.getMessage(), e);
    }
  }

  //테스크 종료시 호출
  @Override
  public void stop() {
    try {
      fileWriter.flush();
    } catch (IOException e) {
      throw new ConnectException(e.getMessage(), e);
    }
  }
}
