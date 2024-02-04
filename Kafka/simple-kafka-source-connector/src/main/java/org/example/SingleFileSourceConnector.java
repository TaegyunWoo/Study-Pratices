/**
 * author         : 우태균
 * description    : 단일 파일에서 데이터를 읽어와 kafka로 produce하는 소스 커넥터
 */
package org.example;

import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.common.config.ConfigException;
import org.apache.kafka.connect.connector.Task;
import org.apache.kafka.connect.errors.ConnectException;
import org.apache.kafka.connect.source.SourceConnector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SingleFileSourceConnector extends SourceConnector {
  private Map<String, String> configProperties;

  //커넥터 버전 명시
  @Override
  public String version() {
    return "1.0";
  }

  //커넥터 초기화
  @Override
  public void start(Map<String, String> props) {
    this.configProperties = props;
    try {
      new SingleFileSourceConnectorConfig(props);
    } catch (ConfigException e) { //필수 설정값이 빠지는 등
      throw new ConnectException(e.getMessage(), e);
    }
  }

  //사용할 테스크 설정
  @Override
  public Class<? extends Task> taskClass() {
    return SingleFileSourceTask.class;
  }

  //테스크별 설정 지정
  @Override
  public List<Map<String, String>> taskConfigs(int maxTasks) {
    List<Map<String, String>> taskConfigs = new ArrayList<>();
    Map<String, String> taskProps = new HashMap<>();

    //여기선 모두 동일한 설정을 사용하도록 지정함
    taskProps.putAll(configProperties);
    for (int i = 0; i < maxTasks; i++) {
      taskConfigs.add(taskProps);
    }

    return taskConfigs;
  }

  //커넥터에서 사용할 설정값 지정
  @Override
  public ConfigDef config() {
    return SingleFileSourceConnectorConfig.CONFIG;
  }

  //커넥터가 종료될 때 실행
  @Override
  public void stop() {
    //ignored
  }
}
