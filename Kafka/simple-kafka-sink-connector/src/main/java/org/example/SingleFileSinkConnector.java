/**
 * author         : 우태균
 * description    : 단일 파일에 저장하는 싱크 커넥터
 */
package org.example;

import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.common.config.ConfigException;
import org.apache.kafka.connect.connector.Task;
import org.apache.kafka.connect.errors.ConnectException;
import org.apache.kafka.connect.sink.SinkConnector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SingleFileSinkConnector extends SinkConnector {
  private Map<String, String> configProperties;

  //커넥터 버전 명시
  @Override
  public String version() {
    return "1.0";
  }

  //커넥터 시작 시 실행, 초기화 설정
  @Override
  public void start(Map<String, String> props) {
    this.configProperties = props;
    try {
      new SingleFileSinkConnectorConfig(props);
    } catch (ConfigException e) {
      throw new ConnectException(e.getMessage(), e);
    }
  }

  //실행할 테스크 등록
  @Override
  public Class<? extends Task> taskClass() {
    return SingleFileSinkTask.class;
  }

  //개별 테스크별 설정 지정
  @Override
  public List<Map<String, String>> taskConfigs(int maxTasks) {
    List<Map<String, String>> taskConfigs = new ArrayList<>();
    Map<String, String> taskProps = new HashMap<>();

    //여기선 모든 테스크에 동일한 설정 등록
    taskProps.putAll(configProperties);
    for (int i = 0; i < maxTasks; i++) {
      taskConfigs.add(taskProps);
    }

    return taskConfigs;
  }

  //커넥터 중단 시 실행
  @Override
  public void stop() {

  }

  //커넥터에서 사용할 설정값 지정
  @Override
  public ConfigDef config() {
    return SingleFileSinkConnectorConfig.CONFIG;
  }
}
