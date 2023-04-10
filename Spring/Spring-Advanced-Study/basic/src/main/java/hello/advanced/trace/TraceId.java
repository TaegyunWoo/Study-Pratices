package hello.advanced.trace;

import java.util.UUID;

public class TraceId {

  private String id; //트랜잭션 id
  private int level; //호출 depth

  public TraceId() {
    this.id = createId();
    this.level = 0;
  }

  private TraceId(String id, int level) {
    this.id = id;
    this.level = level;
  }

  //식별자 랜덤 생성
  private String createId() {
    return UUID.randomUUID().toString().substring(0, 8);
  }

  public TraceId createNextId() {
    return new TraceId(id, level + 1);
  }

  public TraceId createPreviousId() {
    return new TraceId(id, level - 1);
  }

  public boolean isFirstLevel() {
    return level == 0;
  }

  public String getId() {
    return id;
  }

  public int getLevel() {
    return level;
  }
}
