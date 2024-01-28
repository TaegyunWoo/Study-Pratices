/**
 * author         : 우태균
 * description    : Record가 전달될 파티션을 정할 커스텀 파티셔너
 */
package org.example;

import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;
import org.apache.kafka.common.InvalidRecordException;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.utils.Utils;

import java.util.List;
import java.util.Map;

public class CustomPartitioner implements Partitioner {
  @Override
  public int partition(String topic, Object key, byte[] keyBytes, Object value, byte[] valueBytes, Cluster cluster) {
    if (keyBytes == null) { //키값이 없는 Record인 경우
      throw new InvalidRecordException("Need message key");
    }
    if (((String) key).equals("Pangyo")) { //만약 키값이 Pangyo 라면
      return 0; //파티션 번호 0번에 전송
    }

    //만약 키값이 Pangyo가 아니라면
    List<PartitionInfo> partitionInfoList = cluster.partitionsForTopic(topic); //topic의 파티션 정보
    int numPartitions = partitionInfoList.size();
    return Utils.toPositive(Utils.murmur2(keyBytes)) % numPartitions; //'키의 해시값' % 'topic의 파티션 개수'로 저장할 파티션 번호 결정
  }

  @Override
  public void close() {

  }

  @Override
  public void configure(Map<String, ?> configs) {

  }
}
