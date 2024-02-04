/**
 * author         : 우태균
 * description    : 단일 파일에서 데이터를 읽어와 kafka로 produce하는 소스 커넥터의 설정 클래스
 */
package org.example;

import org.apache.kafka.common.config.AbstractConfig;
import org.apache.kafka.common.config.ConfigDef;

import java.util.Map;

public class SingleFileSourceConnectorConfig extends AbstractConfig {
  public static final String DIR_FILE_NAME = "file";
  private static final String DIR_FILE_NAME_DEFAULT_VALUE = "/tmp/kafka.txt";
  private static final String DIR_FILE_NAME_DOC = "source.txt"; //읽을 파일 경로 및 이름
  public static final String TOPIC_NAME = "topic";
  private static final String TOPIC_DEFAULT_VALUE = "test";
  private static final String TOPIC_DOC = "file_source"; //보낼 토픽 이름

  public static ConfigDef CONFIG = new ConfigDef().define(
        DIR_FILE_NAME,
        ConfigDef.Type.STRING,
        DIR_FILE_NAME_DEFAULT_VALUE,
        ConfigDef.Importance.HIGH,
        DIR_FILE_NAME_DOC
      ).define(TOPIC_NAME,
        ConfigDef.Type.STRING,
        TOPIC_DEFAULT_VALUE,
        ConfigDef.Importance.HIGH,
        TOPIC_DOC
  );

  public SingleFileSourceConnectorConfig(Map<String, String> props) {
    super(CONFIG, props);
  }
}
