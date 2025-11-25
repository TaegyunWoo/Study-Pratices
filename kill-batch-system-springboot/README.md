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

- [src/main/java/com/system/batch/tasklet/ZombieProcessCleanupTasklet.java](src/main/java/com/system/batch/tasklet/ZombieProcessCleanupTasklet.java)
- [src/main/java/com/system/batch/config/ZombieBatchConfig.java](src/main/java/com/system/batch/config/ZombieBatchConfig.java)

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

## JobLauncherApplicationRunner

### JobLauncherApplicationRunner 란?

- JobLauncherApplicationRunner 는 Spring Boot 가 제공하는 ApplicationRunner의 한 종류이다.
- 커맨드라인으로 전달된 Spring Batch 잡 파라미터를 해석하고 이를 바탕으로 실제 Job을 실행하는 역할을 수행한다.

### JobLauncherApplicationRunner 가 수행하는 처리 과정

1. Job 목록 준비
    - Spring Boot에 의해 ApplicationContext에 등록된 모든 Job 타입 Bean이 JobLauncherApplicationRunner 에 자동 주입된다.
2. 유효성 검증
    - 만약 애플리케이션 컨텍스트에 Job 타입의 Bean 이 여러개인 경우 : `--spring.batch.job.name` 을 지정하지 않으면 검증 실패
    - 만약 애플리케이션 컨텍스트에 Job 타입의 하나인 경우 : `--spring.batch.job.name` 생략 가능
3. 명령어 해석
    - 커맨드라인으로 전달된 값들을 파싱한다.
    - DefaultJobParametersConverter 을 사용하여, `key=value` 형식의 인자들을 적절한 타입의 JobParameter 로 변환한다.
4. Job 실행
    - 1번에서 주입받은 Job 목록 중, `--spring.batch.job.name` 에 해당하는 Job을 찾는다.
    - 해당 Job을 변환된 JobParameters 와 함께 실행한다.

## Job Parameter 를 가져오는 방법

### `@Value` 사용

```java
@Bean
@StepScope //꼭 함께 사용해야함.
public Tasklet terminatorTasklet1(
    @Value("#{jobParameters['terminatorId']}") String terminatorId,
    @Value("#{jobParameters['targetCount']}") Integer targetCount
) { ... }
```

### Context 에서 가져오기

```java
@Component
public class SystemDestructionTasklet implements Tasklet {
    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        //스텝 컨텍스트 객체로부터 가져온다.
        JobParameters jobParameters = chunkContext.getStepContext()
            .getStepExecution()
            .getJobParameters();

        String targetSystem = jobParameters.getString("system.target");
        long desctructionLevel = jobParameters.getLong("system.destruction.level");
     
        //...
    }
}
```
## Job Parameter 값 검증

### 직접 Validator 구현하는 방법

#### 1. JobParametersValidator 구현

```java
@Component
public class MyJobParametersValidator implements JobParametersValidator {
    @Override
    public void validate(JobParameters parameters) throws JobParametersInvalidException {
        if (parameters.getLong("system.destruction.level") < 0) {
            throw new JobParametersInvalidException("system.destruction.level must be greater than or equal to 0");
        }
    }
}
```

#### 2. JobParametersValidator 를 Job에 등록

```java
@Bean
public Job processTerminatorJob(JobRepository jobRepository, Step terminationStep, MyJobParametersValidator myJobParametersValidator) {
    return new JobBuilder("processTerminatorJob", jobRepository)
        .validator(systemDestructionValidator) //잡 파라미터 검증기 등록
        .start(terminationStep)
        .build();
}
```

### 기본 Validator 사용

Spring Batch 에서는 `DefaultJobParametersValidator` 이라는 기본 구현체를 제공한다.

단순히 파라미터의 존재 여부만 확인하며 될 때는 이걸 사용하면 된다.

#### 예시 코드

```java
@Bean
public Job systemDestructionJob(
    JobRepository jobRepository, 
    Step systemDestructionStep
) {
    return new JobBuilder("systemDestructionJob", jobRepository)
        .validator(new DefaultJobParametersValidator(
            new String[]{"destructionPower", "other param1"},  // 필수 파라미터
            new String[]{"targetSystem", "other param2"}       // 선택적 파라미터
        ))
        .start(systemDestructionStep)
        .build();
}
```

- `DefaultJobParametersValidator` 생성자의 첫번째 파라미터로 전달한 값이 필수 파라미터가 된다.
- `DefaultJobParametersValidator` 생성자의 두번째 파라미터로 전달한 값이 선택적 파라미터가 된다.

# Job 과 Step 의 Scope

---

일반적인 Spring 애플리케이션은 싱글톤 기본 스코프를 제공한다.

하지만 Spring Batch 는 이와 다르게, **JobScope** 와 **StepScope** 를 제공한다.

## JobScope 와 StepScope 의 라이프사이클

- JobScope 와 StepScope 가 선언된 Bean 은 애플리케이션 구동 시점에서는 우선 프록시 객체로만 존재한다.
- 그 후, Job 이나 Step 이 실행된 후에 프록시 객체에 접근을 시도하면 그때 실제 Bean 이 생성된다.

아래는 전체적인 라이프사이클이다.

![img.png](img/img2.png)

## `@JobScope`

`@JobScope` 는 Job이 실행될 때 실제 빈이 생성되고, Job이 종료될 때 함께 제거되는 스코프이다. 즉, JobExcution 과 생명주기를 같이 한다.

```java
@Bean
@JobScope
public Step systemDestructionStep(
    @Value("#{jobParameters['destructionPower']}") Long destructionPower
) {
    return new StepBuilder("systemDestructionStep", jobRepository)
        .tasklet((contribution, chunkContext) -> {
            log.info("시스템 파괴 프로세스가 시작되었습니다. 파괴력: {}", destructionPower);
            return RepeatStatus.FINISHED;
        }, transactionManager)
        .build();
}
```

위 코드에 선언된 `@JobScope` 로 발생하는 효과는 아래와 같다.

- **지연된 빈 생성 (Lazy Bean Creation)**
  - `@JobScope` 가 적용된 빈은 애플리케이션 구동 시점에서 프록시 객체만 생성된다.
  - 빈의 실제 인스턴스는 Job이 실행될 때 생성되고, Job이 종료되면 소멸한다.
- **Job 파라미터와의 연동**
  - 빈이 지연 생성되니, **애플리케이션 실행 중에 전달되는 JobParameters를 Job 실행 시점에 생성되는 `systemDestructionStep` 빈에 넣어줄 수 있게 된다.**
- **병렬처리 지원**
  - 여러 쓰레드가 "동일한 Job 정의"를 실행하더라도, 각각의 JobExecution 마다 서로 다른 `systemDestructionStep` 빈이 생성된다. 따라서 동시성 문제에 대해 안전해진다.

아래 예시로 좀더 쉽게 이해될 것이다.

### RestAPI로 요청을 받아서, 동적으로 Batch 를 실행하는 예시

1. SpringMVC 와 SpringBatch 로 구현된 하나의 애플리케이션이 존재.
2. 애플리케이션 실행
3. `@JobScope` 가 적용된 Bean 은 즉시 초기화되는 것이 아니라, 프록시 상태로 존재하게 됨.
   - 이 시점에서는 아직 JobParameter 를 전달받지 못했다. (RestAPI로 요청이 들어와야 알 수 있으므로)
   - 따라서 `@Value` 로 전달받을 JobParameter 값을 아직 모르고, 그렇기 때문에 프록시 객체로 생성되어야 한다.
4. 동시에 여러 요청 A, B가 들어왔고, 각각 다른 JobParameter 를 전달한다.
   - 이 요청에 따라, 각 쓰레드는 서로 동일한 "Job 정의" 를 실행하여 각각의 JobExecution 이 동작한다.
   - `@JobScope` 가 적용된 Bean 은 JobExecution 마다 생성/초기화되기 때문에 동시성 문제에서 자유롭다.

## `@StepScope`

`@StepScope` 는 `@JobScope` 와 동일한 방식으로 동작하지만, 적용 범위에 있어 차이가 있다.

- `@JobScope` : Job 의 실행범위에서 빈을 관리한다.
- `@StepScope` : Step 의 실행범위에서 빈을 관리한다.

### 예시 코드

```java
@Bean
public Step infiltrationStep(
    JobRepository jobRepository,
    PlatformTransactionManager transactionManager,
    Tasklet systemInfiltrationTasklet
) {
    return new StepBuilder("infiltrationStep", jobRepository)
        .tasklet(systemInfiltrationTasklet, transactionManager)
        .build();
}

@Bean 
@StepScope 
public Tasklet systemInfiltrationTasklet(
    @Value("#{jobParameters['infiltrationTargets']}") String infiltrationTargets
) {
    return (contribution, chunkContext) -> {
        String[] targets = infiltrationTargets.split(",");
        log.info("시스템 침투 시작");
        log.info("주 타겟: {}", targets[0]);
        log.info("보조 타겟: {}", targets[1]);
        log.info("침투 완료");
        return RepeatStatus.FINISHED;
    };
}
```

- `systemInfiltrationTasklet` 에 `@StepScope` 가 적용되어 있다. 이 Tasklet 빈이 Step 의 생명주기와 함께한다는 것을 알 수 있다.
- 즉, 각각의 Step 마다 새로운 `systemInfiltrationTasklet` 이 생성되고, Step 이 종료될 때 함께 제거된다.
- 만약 동시에 여러 Step이 실행되면서 `systemInfiltrationTasklet` 을 사용한다고 해도, `@StepScope` 가 있기 때문에 각 Step 실행마다 독립적인 Tasklet 인스턴스가 생성된다. 따라서 어떠한 동시성 이슈도 발생하지 않는다.

## JobScope 와 StepScope 사용시 주의사항

### 1. 프록시 대상의 타입이 클래스인 경우, 반드시 상속 가능한 클래스여야 한다.

- `@JobScope` 와 `@StepScope` 모두 프록시 객체를 만들기 위해서, CGLIB 를 사용해 클래스 기반의 프록시를 생성한다.
- 따라서 프록시 생성을 위해 반드시 상속 가능한 클래스여야 한다.

### 2. Step 빈에는 `@StepScope` 와 `@JobScope` 를 사용하면 안된다.

#### 2-1. Step 빈에 `@StepScope` 를 설정하는 경우

```java
@Bean
@StepScope
public Step systemDestructionStep(
    SystemInfiltrationTasklet tasklet
) {
    return new StepBuilder("systemDestructionStep", jobRepository)
        .tasklet(tasklet, transactionManager)
        .build();
}
```

- 위 코드를 보면 "Step 정의 빈"에 `@StepScope` 를 적용했다. 이 경우, 오류가 발생하게 된다.
- 이유는 당연하다. 아직 Step 이 없는데, StepScope 를 찾으니 그렇다.

![img.png](img/img3.png)

#### 2-2. Step 빈에 `@JobScope` 를 설정하는 경우

```java
@Bean
@JobScope  // Step에 @JobScope를 달았다
public Step systemDestructionStep(
   @Value("#{jobParameters['targetSystem']}") String targetSystem
) {
   return new StepBuilder("systemDestructionStep", jobRepository)
       .tasklet((contribution, chunkContext) -> {
           log.info("{} 시스템 침투 시작", targetSystem);
           return RepeatStatus.FINISHED;
       }, transactionManager)
       .build();
}
```

- 이번에는 "Step 정의 빈"에 `@JobScope` 를 적용했다. 이 경우, 실행시 오류가 발생하지는 않는다. (Job 실행 후, JobScope 를 찾았으므로)
- 하지만 이 경우, 아래 상황에서 문제가 발생할 수 있다. (자세한 것은 추후 설명한다.)
  - JobOperator를 통한 Step 실행 제어 시
  - Spring Integration(Remote Partitioning)을 활용한 배치 확장 기능 사용 시
- 따라서 위 코드를 아래와 같이 Tasklet 을 사용하도록 수정해야 한다.

```java
@Bean
public Step systemDestructionStep(
    SystemInfiltrationTasklet tasklet  // Tasklet을 주입받는다
) {
    return new StepBuilder("systemDestructionStep", jobRepository)
        .tasklet(tasklet, transactionManager)
        .build();
}

@Slf4j
@Component
@StepScope  // Tasklet에 @StepScope를 달았다
public class SystemInfiltrationTasklet implements Tasklet {
    private final String targetSystem;

    public SystemInfiltrationTasklet(
        @Value("#{jobParameters['targetSystem']}") String targetSystem
    ) {
        this.targetSystem = targetSystem;
    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext context) {
        log.info("{} 시스템 침투 시작", targetSystem);
        return RepeatStatus.FINISHED;
    }
}
```

## 컴파일 시점에 없는 Job Parameter 값을 참조하는 방법

JobBuilder 빈에서 StepBuilder 빈을 참조할 때, 아래와 같이 처리할 수 있다.

```java
// 1. 빈 주입 방식
@Bean
public Job systemTerminationJob(Step systemDestructionStep) {  // Spring이 주입
    return new JobBuilder("systemTerminationJob", jobRepository)
            .start(systemDestructionStep)
            .build();
}

// 2. 메서드 직접 호출 방식
@Bean
public Job systemTerminationJob() {
    return new JobBuilder("systemTerminationJob", jobRepository)
            .start(systemDestructionStep(null))  // Job 파라미터 자리에 null 전달
            .build();
}
```

null을 전달하여 당장의 코드 레벨에서의 참조를 만족시키면, 실제 값은 Job이 실행될 때 입력받은 JobParameters의 값으로 주입된다.

## JobScope 와 StepScope 빈에서 ExecutionContext 의 데이터에 접근하는 방법

### ExecutionContext 란?

- 비즈니스 로직 처리 중에 발생하는 커스텀 데이터를 관리할 방법이 필요한데, 이때 사용하는 것이 바로 ExecutionContext라는 데이터 컨테이너다.
- ExecutionContext를 활용하면 커스텀 컬렉션의 마지막 처리 인덱스나 집계 중간 결과물 같은 데이터를 저장할 수 있다.
- 이는 Job이 중단된 후 재시작할 때 특히 유용하다. Spring Batch가 재시작 시 ExecutionContext의 데이터를 자동으로 복원하므로, 중단된 지점부터 처리를 이어갈 수 있기 때문이다.
- ExecutionContext도 메타데이터 저장소에서 관리하여, 안전하게 보존된다.

### ExecutionContext 에서 데이터 꺼내기

```java
@Bean
@JobScope  
public Tasklet systemDestructionTasklet(
  @Value("#{jobExecutionContext['previousSystemState']}") String prevState
) {
  // JobExecution의 ExecutionContext에서 이전 시스템 상태를 주입받는다
}

@Bean
@StepScope
public Tasklet infiltrationTasklet(
  @Value("#{stepExecutionContext['targetSystemStatus']}") String targetStatus
) {
  // StepExecution의 ExecutionContext에서 타겟 시스템 상태를 주입받는다
}

@Bean
@StepScope
public Tasklet infiltrationTasklet(
        @Value("#{jobExecutionContext['previousSystemState']}") String prevState
) {
    // StepScope 에서도 JobExecution의 ExecutionContext에 접근할 수 있다.
}
```

위 코드의 `jobExecutionContext`와 `stepExecutionContext` 는 각각 다른 범위를 가진다.

- `@JobScope` 에서 가져온 ExecutionContext : Job에 속한 모든 컴포넌트(Step, Tasklet 등등...)에서 `@Value("#{jobExecutionContext['key']}")` 로 접근할 수 있다.
- `@StepScope` 에서 가져온 ExecutionContext : 해당 Step에 속한 컴포넌트에서만 접근할 수 있다.
- Step ExecutionContext 의 데이터 접근 제한 : Step 간의 데이터 독립성을 보장하기 위해, 아래와 같은 제약사항이 있다.
  - Step의 ExecutionContext에 저장된 데이터는 `@Value("#{jobExecutionContext['key']}")` 로 접근할 수 없다. 즉, Step 수준의 데이터를 Job 수준에서 가져올 수 없다.
  - 한 Step의 ExecutionContext는 다른 Step에서 접근할 수 없다. 예를 들어, StepA의 ExecutionContext에 저장된 데이터를 StepB에서 `@Value("#{stepExecutionContext['key']}")` 로 가져올 수 없다.

# Spring Batch Listener

---

## Spring Batch Listener 란?

리스너는 배치 처리의 주요 순간들을 관찰하고, 각 시점에 필요한 동작을 정의할 수 있는 강력한 도구이다.

즉, 배치 처리 중 발생하는 특정 이벤트를 감지하고 원하는 로직을 실행할 수 있게 해준다.

이를 통해, 로깅, 모니터링, 알림 전송, 리소스 정리 등 다양한 부가 기능을 손쉽게 구현할 수 있다.

## Spring Batch가 제공하는 주요 Listener 인터페이스

### JobExecutionListener

```java
public interface JobExecutionListener {
    default void beforeJob(JobExecution jobExecution) { }
    default void afterJob(JobExecution jobExecution) { }
}
```

- Job의 시작과 종료 시점에 호출되는 리스너 인터페이스이다.
- `beforeJob()` 메서드는 Job이 시작되기 전에 호출되며, 초기화 작업이나 로깅 등에 활용할 수 있다.
- `afterJob()` 메서드는 Job이 종료된 후에 호출되며, 결과 처리나 정리 작업 등에 활용할 수 있다.
  - 특히, Job의 성공/실패 여부와 관계없이 무조건 호출된다.

### StepExecutionListener

```java
public interface StepExecutionListener extends StepListener {
    default void beforeStep(StepExecution stepExecution) {
    }

    @Nullable
    default ExitStatus afterStep(StepExecution stepExecution) {
	return null;
    }
}
```

- Step의 시작과 종료 시점에 호출되는 리스너 인터페이스이다.
- `beforeStep()` 메서드는 Step이 시작되기 전에 호출되며, Step 초기화 작업이나 로깅 등에 활용할 수 있다.
- `afterStep()` 메서드는 Step이 종료된 후에 호출되며, 결과 처리나 정리 작업 등에 활용할 수 있다.
  - Step의 성공/실패 여부와 관계없이 무조건 호출된다.

### ChunkListener

```java
public interface ChunkListener extends StepListener {
    default void beforeChunk(ChunkContext context) {
    }

    default void afterChunk(ChunkContext context) {
    }
	
    default void afterChunkError(ChunkContext context) {
    }
}
```

- 청크 단위 처리의 시작과 종료 시점에 호출되는 리스너 인터페이스이다.
- `beforeChunk()` 메서드는 청크 처리가 시작되기 전에 호출되며, 초기화 작업이나 로깅 등에 활용할 수 있다.
- `afterChunk()` 메서드는 트랜잭션이 커밋된 후에 호출된다.
- `afterChunkError()` 메서드는 청크 트랜잭션이 롤백된 이후에 호출된다.

> ChunkListener 는 청크 지향 Step 뿐만 아니라, Tasklet 지향 Step 에서도 사용할 수 있다.
> Tasklet 의 `execute()` 메서드 호출 전후, 그리고 예외 발생 시점에 각각 호출된다.
> 또한 RepeatStatus.CONTINUABLE 을 반환하여 반복 실행되는 Tasklet 에서도 매 반복마다 호출된다. (StepExecutionListener 는 Step 의 시작/종료 시점에만 호출되므로, 반복되는 Tasklet 이라도 한번만 호출된다.)

### ItemReadListener, ItemProcessListener, ItemWriteListener

```java
// ItemReadListener.java
public interface ItemReadListener<T> extends StepListener {
    default void beforeRead() { }
    default void afterRead(T item) { }
    default void onReadError(Exception ex) { }
}

// ItemProcessListener.java
public interface ItemProcessListener<T, S> extends StepListener {
    default void beforeProcess(T item) { }
    default void afterProcess(T item, @Nullable S result) { }
    default void onProcessError(T item, Exception e) { }
}

// ItemWriteListener.java
public interface ItemWriteListener<S> extends StepListener {
    default void beforeWrite(Chunk<? extends S> items) { }
    default void afterWrite(Chunk<? extends S> items) { }
    default void onWriteError(Exception exception, Chunk<? extends S> items) { }
}
```

- 청크 지향 처리 Step에서 각각 읽기, 처리, 쓰기 작업의 시작과 종료 시점에 호출되는 리스너 인터페이스들이다.
- 각 메서드들은 아이템 단위의 처리 전후와 에러 발생 시점에 호출된다.
- 주요 포인트
  - `ItemReadListener.afterRead()` : `ItemReader.read()` 호출 후에 호출되지만, 더이상 읽을 데이터가 없어 `null`을 반환하는 경우에는 호출되지 않는다.
  - `ItemProcessListener.afterProcess()` : `ItemProcessor.process()` 메서드가 `null`을 반환하는 경우에도 호출된다. (즉, 해당 아이템이 필터링된 경우에도 호출된다.)
  - `ItemWriteListener.afterWrite()` : 트랜잭션이 커밋되기 전, 그리고 `ChunkListener.afterChunk()` 보다 먼저 호출된다.

### 각 리스너들의 호출 시점

![img.png](img/img4.png)

### 리스너 활용 예시

- 단계별 모니터링 및 추적 : 처리 단계별 로깅 등
- 실행 결과에 따른 후속 처리
- 데이터 가공 및 전달 : 실제 처리 로직 전후에 데이터를 추가로 정제하거나 변환할 수 있다. Step간 데이터 전달이나 다음 처리에 필요한 정보를 미리 준비할 수 있다.
- 부가 기능 분리

## 배치 리스너 구현 방법

### 구현 방법 1) 전용 리스너 인터페이스를 직접 구현하는 방식

- [src/main/java/com/system/batch/lesson/listener/BigBrotherJobExecutionListener.java](src/main/java/com/system/batch/lesson/listener/BigBrotherJobExecutionListener.java)
- [src/main/java/com/system/batch/lesson/listener/BigBrotherStepExecutionListener.java](src/main/java/com/system/batch/lesson/listener/BigBrotherStepExecutionListener.java)
- 리스너 등록
  - ```java
    @Bean
    public Job systemMonitoringJob(JobRepository jobRepository, Step monitoringStep) {
        return new JobBuilder("systemMonitoringJob", jobRepository)
            .listener(new BigBrotherJobExecutionListener())
            .start(monitoringStep)
            .build();
    }
    ```

### 구현 방법 2) 리스너 특화 애너테이션을 사용하는 방식

- [src/main/java/com/system/batch/lesson/listener/ServerRackControlListener.java](src/main/java/com/system/batch/lesson/listener/ServerRackControlListener.java)
- [src/main/java/com/system/batch/lesson/listener/ServerRoomInfiltrationListener.java](src/main/java/com/system/batch/lesson/listener/ServerRoomInfiltrationListener.java)
- 리스너 등록
  - ```java
    @Bean
    public Step serverRackControlStep(Tasklet destructiveTasklet) {
        return new StepBuilder("serverRackControlStep", jobRepository)
            .tasklet(destructiveTasklet(), transactionManager)
            .listener(new ServerRackControlListener()) // 빌더의 listener() 메서드에 전달
            .build();
    }
    ```
- 제공 애너테이션 종류
  - ```
    @AfterChunk, @AfterChunkError, @AfterJob, @AfterProcess, @AfterRead, @AfterStep, @AfterWrite, @BeforeChunk, @BeforeJob, @BeforeProcess, @BeforeRead, @BeforeStep, @BeforeWrite, @OnProcessError, @OnReadError, @OnSkipInProcess, @OnSkipInRead, @OnSkipInWrite
    ```
    
## JobExecutionListener 와 ExecutionContext 를 활용한 동적 데이터 전달

잡 파라미터만으로는 전달할 수 없는 동적인 데이터가 필요한 경우, `JobExecutionListener` 의 `beforeJob()` 메서드를 활용하면 추가적인 동적 데이터를 각 Step에 전달할 수 있다.

### 예시 코드

- [JobExecutionListener 구현체](src/main/java/com/system/batch/lesson/listener/InfiltrationPlanListener.java)
- [Job 설정 코드](src/main/java/com/system/batch/lesson/listener/AdvancedSystemInfiltrationConfig.java)

## JobParameter 가 아닌 ExecutionContext 를 사용하는 이유

- 배치 작업의 재현 가능성과 일관성을 보장하기 위해, **JobParameter 는 불변성**을 갖기 때문이다.
  - 재현 가능성: 동일한 JobParameters로 실행한 Job은 항상 동일한 결과를 생성해야 한다. 실행 중간에 JobParameters가 변경되면 이를 보장할 수 없다.
  - 추적 가능성: 배치 작업의 실행 기록(JobInstance, JobExecution)과 JobParameters는 메타데이터 저장소에 저장된다. JobParameters가 변경 가능하다면 기록과 실제 작업의 불일치가 발생할 수 있다.
- **따라서, 실행 시점에 동적으로 결정되는 데이터는 JobParameters가 아닌 ExecutionContext에 저장하여 전달하는 것이 바람직하다.**

### ExecutionContext 사용시 주의사항

#### 잘못된 코드

```java
@Override
public void beforeJob(JobExecution jobExecution) {
    jobExecution.getExecutionContext()
        .put("targetDate", LocalDate.now()); // 치명적인 실수
}
```

- 위 코드는 `LocalDate.now()` 를 ExecutionContext 에 저장하고 있다.
- 문제점
  - `LocalDate.now()` 는 실행 시점에 따라 값이 달라진다.
  - 따라서, Job 이 재시작될 때마다 ExecutionContext 에 저장된 값이 달라질 수 있다.
  - 이는 배치 작업의 재현 가능성과 일관성을 해친다. 어제 데이터를 다시 처리하고 싶으면? 불가능하다. 프로그램을 수정하지 않으면 그날의 데이터를 다시 처리할 수 없다.

### 올바른 코드

```shell
# 외부에서 파라미터로 받는다.
./gradlew bootRun --args='--spring.batch.job.name=systemInfiltrationJob -date=2024-10-13'
```

- 외부에서 날짜 값을 전달받으면, 배치 작업의 유연성을 극대화할 수 있다.
- JobParameters를 사용할 수 있다면 그 방법을 사용하라. 외부에서 값을 받는 것이 훨씬 더 안전하고 유연하다.
- JobExecutionListener와 ExecutionContext는 외부에서 값을 받을 수 없는 경우에만 사용하자.

## 보다 간결하게 ExecutionContext 가져오기

Step 에서 스텝간 공유할 수 있는 ExecutionContext 를 가져올때, Step의 ExecutionContext 을 먼저 가져오고, 다시 거기서 Job 수준의 ExecutionContext 를 가져오는 번거로운 과정을 거쳐야 한다.

즉, `chunkContext.getStepContext().getStepExecution().getJobExecution().getExecutionContext()` 와 같이 긴 코드를 작성하는 것은 번거롭다.

ExecutionContextPromotionListener 를 사용해서, 보다 간결하게 스텝간 데이터 공유를 할 수 있다.

### ExecutionContextPromotionListener 란?

- Step 수준 ExecutionContext 의 데이터를 Job 수준 ExecutionContext 로 **승격(Promote)** 시켜주는 리스너이다.
  - Spring Batch에서는 Step 수준의 ExecutionContext 데이터를 Job 수준의 ExecutionContext로 옮기는 과정을 승격(Promote)이라 부른다.
- `ExecutionContextPromotionListener` 의 `setKeys()` 메서드를 사용하여, 승격시킬 키 목록을 지정할 수 있다.
- [사용 예시코드](src/main/java/com/system/batch/lesson/listener/SystemTerminationConfig.java)

![img.png](img/img4.png)

### ExecutionContextPromotionListener 사용시 주의사항

각 Step은 가능한 한 독립적으로 설계하여 재사용성과 유지보수성을 높이는 것이 좋다.

불가피한 경우가 아니라면 Step 간 데이터 의존성은 최소화하는 것이 좋다.

Step 간 데이터 공유가 늘어날수록 복잡도가 증가한다.

## Listener 와 `@JobScope`, `@StepScope` 통합

리스너 구현체에 `@JobScope` 또는 `@StepScope` 애너테이션을 적용할 수 있다.

이를 통해 "실행 시점에 결정되는 값(잡 파라미터)"들을 리스너 내에서도 활용할 수 있다.

- [예시 코드](src/main/java/com/system/batch/lesson/listener/SystemDestructionConfig.java)

## 리스너를 효과적으로 다루는 방법

### 적절한 리스너 사용

작업 범위와 목적에 따라 적절한 리스너를 선택해야한다.

- JobExecutionListener: 전체 작업의 시작과 종료
- StepExecutionListener: 각 작업 단계의 실행
- ChunkListener: 작업을 청크단위로 실행할 때, 반복의 시작과 종료 시점을 통제
- Item[Read|Process|Write]Listener: 개별 아이템 식별 통제

### 예외 처리는 신중하게

`JobExecutionListener`의 `beforeJob()`과 `StepExecutionListener`의 `beforeStep()`에서 예외가 발생하면 Job과 Step이 실패한 것으로 판단된다.

하지만 모든 예외가 Step을 중단시켜야 할 만큼 치명적인 것은 아니다. 이런 경우는 직접 예외를 잡아서 무시하고 진행하는 것이 현명하다.

> `JobExecutionListener.afterJob()`과 `StepExecutionListener.afterStep()`에서 발생한 예외는 무시된다. 즉, 예외가 발생해도 Job과 Step의 실행 결과에 영향을 미치진 않는다.

```java
@Override
public void beforeStep(StepExecution stepExecution) {
    try {
        // 치명적인 로직 수행
        systemMetricCollector.collect();
    } catch (Exception e) {
        // 심각하지 않은 예외는 로그만 남기고 진행
        log.warn("메트릭 수집 실패. 작전은 계속 진행: {}", e.getMessage());
        // 정말 심각한 문제면 예외를 던져서 Step을 중단시킨다
        // throw new RuntimeException("치명적 오류 발생", e);
    }
}
```

### 단일 책임 원칙 준수

리스너는 감시와 통제만 담당한다. 비즈니스 로직은 분리하는 것이 좋다. 리스너가 너무 많은 일을 하면 유지보수가 어려워지고 시스템 동작을 파악하기 힘들어진다.

#### 성능 최적화) 실행 빈도 고려

- `JobExecutionListener` / `StepExecutionListener`
  - Job, Step 실행당 한 번씩만 실행되므로 비교적 안전하다
  - 무거운 로직이 들어가도 전체 성능에 큰 영향 없음
- `ItemReadListener` / `ItemProcessListener`
  - 매 아이템마다 실행되므로 치명적일 수 있다

```java
// 이런 코드는 시스템을 마비시킬 수 있다
@Override
public void afterRead(Object item) {
    heavyOperation();  // 매 아이템마다 실행되면 시스템이 마비된다
    remoteApiCall();   // 외부 API 호출은 더더욱 위험
}
```

### 성능 최적화) 리소스 사용 최소화

- 데이터베이스 연결, 파일 I/O, 외부 API 호출은 최소화
- 리스너 내 로직은 가능한 한 가볍게 유지하라
- 특히 Item 단위 리스너에서는 더욱 중요하다


// FlatFileItemReader

#### 고정 길이 형식의 파일 읽기

```
ERR001  2024-01-19 10:15:23  CRITICAL  1234  SYSTEM  CRASH DETECT \n
ERR002  2024-01-19 10:15:25  FATAL     1235  MEMORY  OVERFLOW FAIL\n
```

위와 같이 단순히 고정 길이 형식의 파일을 읽어야 하는 경우, `FlatFileItemReader.fixedLength()` 를 사용할 수 있다.

[예시코드](src/main/java/com/system/batch/lesson/flatfileitemreader/FixedLengthSystemFailureJobConfig.java)

#### 정규식으로 파일 읽기

```
[WARNING][Thread-156][CPU: 78%] Thread pool saturation detected - 45/50 threads in use...
[ERROR][Thread-157][CPU: 92%] Thread deadlock detected between Thread-157 and Thread-159
[FATAL][Thread-159][CPU: 95%] Thread dump initiated - system unresponsive for 30s
```

위와 같이 정규식 패턴으로 파일을 읽어야 하는 경우, `RegexLineTokenizer` 를 사용할 수 있다.

[예시코드](src/main/java/com/system/batch/lesson/flatfileitemreader/RegexSystemLogJobConfig.java)




# 파일 기반 배치 처리

---

## FlatFileItemReader 와 FlatFileItemWriter

파일 기반 배치 처리의 기본적인 Reader/Writer 는 FlatFileItemReader 와 FlatFileItemWriter 이다.

### FlatFile 이란?

먼저 FlatFile 이란 아래와 같다.

- CSV 파일처럼 단순하게 행열로 구성된 파일이다.
- 각 라인이 하나의 데이터이다.
- 다양한 구분자를 지원한다. (`,` , `\t` 등)
- 강력한 호환성과 범용성

### FlatFileItemReader

FlatFileItemReader 는 플랫 파일로부터 데이터를 읽어오는 기본적인 Reader 이다.

#### `FlatFileItemReader.doRead()`

- 파일에서 한 줄을 읽어온다.
- 읽어온 한 줄의 문자열을 우리가 사용할 객체로 변환해 리턴한다. 

아래는 `doRead()` 함수의 예시 코드이다.

```java
...
String line = readLine(); // 한 줄의 데이터를 읽어온다.
...
// 문자열을 도메인 객체로 변환해 리턴한다. 
return lineMapper.mapLine(line, lineCount); 
```

`doRead()` 는 읽어들인 문자열을 `lineMapper.mapLine()` 로 도메인 객체로 변환하게 된다.

SpringBatch 는 LineMapper 인터페이스의 기본적인 구현체 `DefaultLineMapper` 를 제공한다.

#### 문자열을 쪼개서 도메인 객체로 매핑하기: `DefaultLineMapper`

`DefaultLineMapper` 의 동작은 크게 두 단계로 나뉜다.

1. 문자열 라인 토큰화
    - e.g) `a,b,c,d` 처럼 구성된 문자열을 쉼표를 기준으로 나눠, `a` , `b` , `c` , `d` 로 토큰화한다.
2. 분리된 각 토큰들을 도메인 객체의 프로퍼티에 매핑한다.

`DefaultLineMapper.mapLine()` 은 아래와 같이 구현되어 있다.

```java
// DefaultLineMapper.java
@Override
public T mapLine(String line, int lineNumber) throws Exception {
    FieldSet fieldSet = tokenizer.tokenize(line);  // 1단계: 문자열을 토큰화해 FieldSet 반환
    return fieldSetMapper.mapFieldSet(fieldSet);  // 2단계: FieldSet을 객체로 매핑	 
}
```

- `tokenizer.tokenize(line)`
  - LineTokenizer 구현체의 메서드 `tokenize()` 를 사용하여 문자열을 토큰화한다.
  - LineTokenizer 는 대표적인 구현체로, `DelimitedLineTokenizer` 와 `FixedLengthTokenizer` 가 있다.
  - `DelimitedLineTokenizer` : 쉼표 문자와 같은 구분자로 구분된 데이터를 토큰화한다.
    - e.g) `ERR001,2024-01-19,CRITICAL` -> `["ERR001", "2024-01-19", "CRITICAL"]`
  - `FixedLengthTokenizer` : 고정 길이로 구분된 데이터를 토큰화한다.
    - e.g) `ERR00120240119CRITICAL` -> `["ERR001", "20240119", "CRITICAL"]` (각각 6자리, 8자리, 8자리)
- `fieldSetMapper.mapFieldSet(fieldSet)`
  - 해당 메서드를 통해서, 토큰화된 필드들을 객체에 매핑한다.
  - 별도의 설정이 없다면, `BeanWrapperFieldSetMapper` 가 기본 FieldSetMapper 로 사용된다.
  - `BeanWrapperFieldSetMapper` 는 자바 빈 규약을 따르는 객체에 데이터를 매핑한다. `setter` 메서드를 호출해서 데이터를 설정하게 된다.
  - 매핑 예시
    - ```java
      /* FieldSet 데이터
       * FieldSet {
       * tokens: ["ERR001", "2024-01-19 10:15:23", "CRITICAL", "1234", "SYSTEM_CRASH"]
       * names: ["errorId", "errorDateTime", "severity", "processId", "errorMessage"]
       * }
       */
    
      // 매핑될 객체
      @Data     // setter 메서드 반드시 필요
      public static class SystemFailure {
      private String errorId;        // "ERR001" 매핑
      private String errorDateTime;  // "2024-01-19 10:15:23" 매핑
      private String severity;       // "CRITICAL" 매핑
      private Integer processId;     // "1234" -> 1234로 타입 변환 후 매핑
      private String errorMessage;   // "SYSTEM_CRASH" 매핑
      }
      ```

#### FlatFileItemReader 원리 정리

![img.png](img/img5.png)

#### 예시코드

[SystemFailureJobConfig](src/main/java/com/system/batch/lesson/flatfileitemreader/SystemFailureJobConfig.java)
