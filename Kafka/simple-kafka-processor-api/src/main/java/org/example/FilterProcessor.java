/**
 * author         : 우태균
 * description    : 필터 역할을 수행하는 프로세서
 */
package org.example;

import org.apache.kafka.streams.processor.api.Processor;
import org.apache.kafka.streams.processor.api.ProcessorContext;
import org.apache.kafka.streams.processor.api.Record;

public class FilterProcessor implements Processor<String, String, String, String> {
  private ProcessorContext<String, String> context; //프로세서 관련 정보

  @Override
  public void process(Record<String, String> record) {
    if (record.value().length() > 5) {
      context.forward(record); //다음 토폴로지로 넘김
    }
    context.commit(); //명시적으로 처리가 완료됨을 기재
  }

  @Override
  public void init(ProcessorContext<String, String> context) {
    this.context = context;
  }

  @Override
  public void close() {
    Processor.super.close();
  }
}
