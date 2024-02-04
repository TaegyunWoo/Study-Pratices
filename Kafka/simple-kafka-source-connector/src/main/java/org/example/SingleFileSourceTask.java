/**
 * author         : 우태균
 * description    : 커넥터가 수행할 실제 작업 Task
 */
package org.example;

import org.apache.kafka.connect.data.Schema;
import org.apache.kafka.connect.errors.ConnectException;
import org.apache.kafka.connect.source.SourceRecord;
import org.apache.kafka.connect.source.SourceTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;


public class SingleFileSourceTask extends SourceTask {
  private Logger logger = LoggerFactory.getLogger(SingleFileSourceTask.class);

  public final String FILENAME_FIELD = "filename"; //파일을 읽은 지점을 저장할 오프셋 스토리지에서 사용될 '파일이름 값의 필드명'
  public final String POSITION_FIELD = "position"; //파일을 읽은 지점을 저장할 오프셋 스토리지에서 사용될 '포지션 값의 필드명'
  private Map<String, String> fileNamePartition; //오프셋 스토리지에서 데이터를 읽고 쓸 때 사용 (key: FILENAME_FIELD, value: 읽은 파일 이름)
  private Map<String, Object> offset; //오프셋 스토리지에서 데이터를 읽고, 쓸 때 사용 (key: position, value: 테스크에서 마지막에 읽은 오프셋)
  private String topic;
  private String file;
  private long position = -1; //현재 커넥터가 읽어야하는 오프셋 값 (초기값을 -1로 설정 -> 처음부터 읽음)

  //테스크 버전 (보통 이 테스크를 실행할 커넥터의 버전과 동일)
  @Override
  public String version() {
    return "1.0.0";
  }

  //테스크 시작을 위한 초기화 설정
  @Override
  public void start(Map<String, String> props) {
    try {
      //Init variables
      SingleFileSourceConnectorConfig config = new SingleFileSourceConnectorConfig(props); //설정 인스턴스 생성
      topic = config.getString(SingleFileSourceConnectorConfig.TOPIC_NAME);
      file = config.getString(SingleFileSourceConnectorConfig.DIR_FILE_NAME);
      fileNamePartition = Collections.singletonMap(FILENAME_FIELD, file);
      offset = context.offsetStorageReader().offset(fileNamePartition);

      //Get file offset from offsetStorageReader
      if (offset != null) { //오프셋이 있다면
        Object lastReadFileOffset = offset.get(POSITION_FIELD);
        if (lastReadFileOffset != null) { //포지션 필드의 값이 있다면
          position = (Long) lastReadFileOffset; //오프셋 스토리지에 저장된 마지막 포지션 값으로 설정
        }

      } else { //오프셋이 없다면
        position = 0; //처음부터 읽도록 설정
      }

    } catch (Exception e) {
      throw new ConnectException(e.getMessage(), e);
    }
  }

  //실제로 수행할 작업 내용
  @Override
  public List<SourceRecord> poll() throws InterruptedException {
    List<SourceRecord> results = new ArrayList<>();
    try {
      Thread.sleep(1000);

      List<String> lines = getLines(position);

      if (lines.size() > 0) {
        lines.forEach(line -> {
          Map<String, Long> sourceOffset = Collections.singletonMap(POSITION_FIELD, ++position);
          SourceRecord sourceRecord = new SourceRecord(fileNamePartition, sourceOffset, topic, Schema.STRING_SCHEMA, line);
          results.add(sourceRecord);
        });
      }
      return results;
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
      throw new ConnectException(e.getMessage(), e);
    }
  }

  //파일을 한 줄씩 읽는 메서드 (오프셋만큼 건너뛴 후)
  private List<String> getLines(long readLine) throws Exception {
    BufferedReader reader = Files.newBufferedReader(Paths.get(file));
    return reader.lines().skip(readLine).collect(Collectors.toList());
  }

  //테스크 종료
  @Override
  public void stop() {

  }
}
