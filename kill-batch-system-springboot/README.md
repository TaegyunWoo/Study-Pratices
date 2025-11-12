# 태스크릿 지향 처리

---

## 태스크릿 지향 처리 특징

- 단순 작업에 적합
  - 태스크릿 지향 처리는 알림 발송, 파일 복사, 오래된 데이터 삭제 등 단순 작업을 처리하는 Step 유형이다.
- Tasklet 인터페이스 구현
  - 태스크릿 지향 처리는 Tasklet 인터페이스를 구현하여 execute() 메서드에서 작업 로직을 정의한다.
  - 이를 `StepBuilder.tasklet()` 메서드를 사용하여 Step에 할당한다.
- RepeatStatus 로 실행 제어
  - `Tasklet.execute()` 메서드는 `RepeatStatus`를 반환하여 작업의 완료 상태를 나타낸다.
  - `RepeatStatus.FINISHED`를 반환하면 작업이 완료되고, `RepeatStatus.CONTINUABLE`를 반환하면 작업이 계속 실행된다.
- 트랜잭션 지원
  - Spring Batch는 `Tasklet.execute()` 메서드 실행전후로 트랜잭션을 시작하고 커밋하여, DB 일관성과 원자성을 보장한다.
  - 만약 단순 파일 작업이나 외부 API 호출 등 트랜잭션이 필요없는 작업이라면, `ResourcelessTransactionManager` 를 사용하여 트랜잭션 오버헤드를 줄일 수 있다.

## 예시 코드

- `com/system/batch/tasklet/ZombieProcessCleanupTasklet.java`
- `com/system/batch/config/ZombieBatchConfig.java`

# 청크 지향 처리

---

## 청크 지향 처리 특징

- 대부분의 배치 작업, 특히 데이터를 다루는 작업은 `읽기 -> 처리 -> 쓰기 패턴`을 따른다.
- `읽기 -> 처리 -> 쓰기 패턴`을 청크 단위로 수행하는 것을 청크 지향 처리라고 한다.
  - 청크 : 데이터를 일정 단위로 쪼갠 덩어리를 말한다.
- 청크 단위 처리시 장점
  - 대량의 데이터를 한번에 메모리에 올리지 않고도 처리할 수 있다.
  - 트랜잭션 단위를 청크 단위로 설정하여, 실패시 재시도 및 복구가 용이하다.
- 청크 지향 처리 구성 요소
  - `ItemReader` : 데이터를 읽어오는 역할을 한다.
  - `ItemProcessor` : 읽어온 데이터를 가공하는 역할을 한다.
  - `ItemWriter` : 가공된 데이터를 저장하는 역할을 한다.

## `ItemReader` : 데이터 읽기

```java
public interface ItemReader<T> {
    T read() throws Exception, 
        UnexpectedInputException, 
        ParseException, 
        NonTransientResourceException;
}
```

- `ItemReader`는 데이터를 한 건씩 읽어오는 역할을 한다.
- `read()` 메서드는 아이템(row)를 한 건씩 반환하며, 더 이상 읽을 데이터가 없으면 `null`을 반환하며 종료를 알린다.
  - `ItemReader`가 `null`을 반환하는 것이 청크 지향 처리 Step의 종료 시점이라는 점에 주의해야 한다.

## `ItemProcessor` : 데이터 가공 및 처리

```java
public interface ItemProcessor<I, O> {
    O process(I item) throws Exception;
}
```

- `ItemProcessor`는 읽어온 데이터를 가공하는 역할을 한다.
- `process()` 메서드는 입력 아이템(`I`)을 받아 가공된 출력 아이템(`O`)을 반환한다.
- 필요에 따라 `null`을 반환하여 해당 아이템을 건너뛸 수도 있다. 즉, `null`을 반환하면 해당 아이템은 `ItemWriter`로 전달되지 않는다. 유효하지 않은 데이터나 생략할 수 있는 데이터를 필터링할 때 유용하다.
- 경우에 따라, 입력 데이터의 유효성을 검사할 때도 사용된다. 조건에 맞지 않는 데이터를 만나는 경우, 예외를 발생시켜 배치 작업을 중단할 수도 있다.
- `ItemProcessor`는 선택적 구성 요소이다. 모든 청크 지향 처리 Step에 반드시 포함될 필요는 없다.
- 청크 크기만큼 `ItemReader.read()` 메서드로 데이터를 모두 읽어온 후에, 각 아이템에 대해 `ItemProcessor.process()` 메서드를 호출하여 가공한다.
  - 한 개의 데이터를 `ItemReader.read()` 가 읽어온 뒤 바로 `ItemProcessor.process()` 가 처리하는 것이 아니다.

## `ItemWriter` : 데이터 쓰기

```java
public interface ItemWriter<T> {
    void write(Chunk<? extends T> chunk) throws Exception;
}
```

- `ItemWriter`는 가공된 데이터를 저장하는 역할을 한다.
- `write()` 메서드는 가공된 아이템들을 청크 단위로 받아, 한 번에 저장한다. (파라미터 타입이 `Chunk` 인 것에 주목하자.)
  - `ItemProcessor` 의 `process()` 메서드는 청크 전체를 입력받지 않고, 아이템 단위로 처리한다.
- `ItemWriter`는 데이터베이스에 저장하거나, 파일에 기록하거나, 외부 시스템에 전송하는 등 다양한 방식으로 데이터를 쓸 수 있다.

## Reader-Processer-Writer 패턴

청크 지향 처리의 핵심은 `ItemReader`, `ItemProcessor`, `ItemWriter`가 협력하여 데이터를 처리하는 것이다.

이를 통한 장점은 다음과 같다.

1. **완벽한 책임 분리** : 각 컴포넌트는 자신의 역할만 수행한다. `ItemReader`는 읽기, `ItemProcessor`는 가공, `ItemWriter`는 쓰기에만 집중한다. 덕분에 코드는 명확해지고 유지보수는 간단해진다.

2. **재사용성 극대화** : 컴포넌트들은 독립적으로 설계되어 있어 어디서든 재사용 가능하다. 새로운 배치를 만들 때도 기존 컴포넌트들을 조합해서 빠르게 구성할 수 있다.

3. **높은 유연성** : 요구사항이 변경되어도 해당 컴포넌트만 수정하면 된다. 데이터 형식이 바뀌면 `ItemProcessor`만, 데이터 소스가 바뀌면 `ItemReader`만 수정하면 된다. 이런 독립성 덕분에 변경에 강하다.

4. **대용량 처리의 표준** : 데이터를 다루는 배치 작업은 결국 '읽고-처리하고-쓰는' 패턴을 따른다. Spring Batch는 이 패턴을 완벽하게 구조화했다.

## 예시 코드

```java
@Bean
public Step processStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
   return new StepBuilder("processStep", jobRepository)
           .<CustomerDetail, CustomerSummary>chunk(10, transactionManager)  // 청크 지향 처리 활성화
           .reader(itemReader())       // 데이터 읽기 담당
           .processor(itemProcessor()) // 데이터 처리 담당
           .writer(itemWriter())      // 데이터 쓰기 담당
           .build();
}

@Bean
public Job customerProcessingJob(JobRepository jobRepository, Step processStep) {
   return new JobBuilder("customerProcessingJob", jobRepository)
           .start(processStep)  // processStep으로 Job 시작
           .build();
}
```

- `reader()`: `ItemReader` 구현체를 전달받아 Step의 읽기 작업을 담당할 컴포넌트로 등록한다.
- `processor()`: `ItemProcessor` 구현체를 전달받아 Step의 처리 작업을 담당할 컴포넌트로 등록한다. `ItemProcessor`가 필요하지 않다면 생략 가능하며, 이 경우 `ItemReader`가 읽은 데이터가 그대로 `ItemWriter`로 전달된다.
- `writer()`: `ItemWriter` 구현체를 전달받아 Step의 쓰기 작업을 담당할 컴포넌트로 등록한다.

## 청크 지향 처리 흐름도

![img.png](img/img.png)

## 청크 지향 처리 반복 흐름도

![img.png](img/img1.png)



# JobParameters

---

## JobParameters 란

- `JobParameters`는 Spring Batch에서 배치 작업 실행 시 전달되는 매개변수 집합을 나타낸다.
- **배치 작업 실행 시점**에 동적으로 값을 주입하여, 동일한 Job 을 다양한 상황에 맞게 실행할 수 있도록 한다.

## 다양한 JobParameters 전달 방법

### 커맨드라인에서 잡 파라미터 전달하기

JobParameters는 커맨드라인에서 `key=value,type` 형식으로 전달할 수 있다.

단, `type`은 생략 가능하며, 생략 시 기본적으로 `String` 타입으로 간주된다.

> `key=valuye,type,identificationFlag` 형식도 지원되며, `identificationFlag`는 해당 파라미터가 Job 식별에 사용되는지 여부를 나타낸다. 기본값은 `true` 이다.
> 자세한 내용은 추후 다룬다.

```
--spring.batch.job.name={jobName} {key1}={value1},{type1} {key2}={value2},{type2} ...
```

아래는 예시이다.

```shell
./gradlew bootRun --args='--spring.batch.job.name=dataProcessingJob inputFilePath=/data/input/users.csv,java.lang.String'
```

커맨드라인 문자열로 전달된 JobParameters는 Spring Batch의 `DefaultJobParametersConverter` 컴포넌트를 통해 적절한 타입으로 변환된다.

자세한 내용은 [DefaultJobParametersConverter](https://docs.spring.io/spring-batch/docs/current/api/org/springframework/batch/core/converter/DefaultJobParametersConverter.html)와 [DefaultConversionService](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/core/convert/support/DefaultConversionService.html) 를 참고하자.

### 기본 파라미터 표기법의 한계

- 파라미터 값에 쉼표 문자가 포함된 경우, 기본 표기법으로는 이를 구분할 수 없다.
- 예를 들어, `filePath=/data/input/file,name.csv,String` 와 같이 쉼표가 포함된 값을 전달하면, Spring Batch는 이를 `filePath` 키에 대한 값으로 올바르게 인식하지 못한다.
- 이 경우, JSON 형식의 JobParameters를 사용할 수 있다.

### JSON 형식으로 JobParameters 전달하기

아래와 같은 형식으로 JSON 문자열을 사용하여 JobParameters를 전달할 수 있다.

```
key1='{ "value":"value1", "type":"java.lang.String", "identifying":true }' key2='{ "value":"value1", "type":"java.lang.String", "identifying":false }''
```