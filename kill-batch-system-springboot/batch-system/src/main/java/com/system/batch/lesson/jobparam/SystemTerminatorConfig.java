package com.system.batch.lesson.jobparam;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.converter.JobParametersConverter;
import org.springframework.batch.core.converter.JsonJobParametersConverter;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
//@Configuration
public class SystemTerminatorConfig {
    @Bean
    public Job processTerminatorJob(JobRepository jobRepository, Step terminationStep, SystemDestructionValidator systemDestructionValidator) {
        return new JobBuilder("processTerminatorJob", jobRepository)
            .validator(systemDestructionValidator) //잡 파라미터 검증기 등록
            .start(terminationStep)
            .build();
    }

    @Bean
    public Step terminationStep(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager, Tasklet terminatorTasklet5) {
        return new StepBuilder("termintationStep", jobRepository)
            .tasklet(terminatorTasklet5, platformTransactionManager)
            .build();
    }

    /**
     * JobParameter를 주입받는 Tasklet 예시 1 - 문자열 및 정수 타입
     *
     * [실행 명령어]
     * <code>./gradlew bootRun --args='--spring.batch.job.name=processTerminatorJob terminatorId=KILL-9,java.lang.String targetCount=5,java.lang.Integer'</code>
     */
    @Bean
    @StepScope
    public Tasklet terminatorTasklet1(
            @Value("#{jobParameters['terminatorId']}") String terminatorId,
            @Value("#{jobParameters['targetCount']}") Integer targetCount
    ) {
        return (contribution, chunkContext) -> {
            log.info("시스템 종결자 정보:");
            log.info("ID: {}", terminatorId);
            log.info("제거 대상 수: {}", targetCount);
            log.info("⚡ SYSTEM TERMINATOR {} 작전을 개시합니다.", terminatorId);
            log.info("☠️ {}개의 프로세스를 종료합니다.", targetCount);

            for (int i = 1; i <= targetCount; i++) {
                log.info("💀 프로세스 {} 종료 완료!", i);
            }

            log.info("🎯 임무 완료: 모든 대상 프로세스가 종료되었습니다.");

            return RepeatStatus.FINISHED;
        };
    }

    /**
     * JobParameter를 주입받는 Tasklet 예시 2 - 날짜/시간 타입
     * 날짜/시간 타입의 잡 파라미터는 ISO 표준 형식으로 전달해야 한다.
     *
     * [실행 명령어]
     * <code>./gradlew bootRun --args='--spring.batch.job.name=processTerminatorJob executionDate=2024-01-01,java.time.LocalDate startTime=2024-01-01T14:30:00,java.time.LocalDateTime'</code>
     */
    @Bean
    @StepScope
    public Tasklet terminatorTasklet2(
            @Value("#{jobParameters['executionDate']}") LocalDate executionDate,
            @Value("#{jobParameters['startTime']}") LocalDateTime startTime
    ) {
        return (contribution, chunkContext) -> {
            log.info("시스템 처형 정보:");
            log.info("처형 예정일: {}", executionDate.format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일")));
            log.info("작전 개시 시각: {}", startTime.format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH시 mm분 ss초")));
            log.info("⚡ {}에 예정된 시스템 정리 작전을 개시합니다.", executionDate);
            log.info("💀 작전 시작 시각: {}", startTime);

            // 작전 진행 상황 추적
            LocalDateTime currentTime = startTime;
            for (int i = 1; i <= 3; i++) {
                currentTime = currentTime.plusHours(1);
                log.info("☠️ 시스템 정리 {}시간 경과... 현재 시각:{}", i, currentTime.format(DateTimeFormatter.ofPattern("HH시 mm분")));
            }

            log.info("🎯 임무 완료: 모든 대상 시스템이 성공적으로 제거되었습니다.");
            log.info("⚡ 작전 종료 시각: {}", currentTime.format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH시 mm분 ss초")));

            return RepeatStatus.FINISHED;
        };
    }

    /**
     * JobParameter를 주입받는 Tasklet 예시 3 - 열거형 타입
     * ENUM 타입은 별도의 변환 로직 없이 바로 주입받을 수 있다.
     *
     * [실행 명령어]
     * <code>./gradlew bootRun --args='--spring.batch.job.name=processTerminatorJob questDifficulty=NORMAL,com.system.batch.lesson.jobparam.QuestDifficulty'</code>
     */
    @Bean
    @StepScope
    public Tasklet terminatorTasklet3(
            @Value("#{jobParameters['questDifficulty']}") QuestDifficulty questDifficulty
    ) {
        return (contribution, chunkContext) -> {
            log.info("⚔️ 시스템 침투 작전 개시!");
            log.info("임무 난이도: {}", questDifficulty);
            // 난이도에 따른 보상 계산
            int baseReward = 100;
            int rewardMultiplier = switch (questDifficulty) {
                case EASY -> 1;
                case NORMAL -> 2;
                case HARD -> 3;
                case EXTREME -> 5;
            };
            int totalReward = baseReward * rewardMultiplier;
            log.info("💥 시스템 해킹 진행 중...");
            log.info("🏆 시스템 장악 완료!");
            log.info("💰 획득한 시스템 리소스: {} 메가바이트", totalReward);
            return RepeatStatus.FINISHED;
        };
    }

    /**
     * JobParameter를 주입받는 Tasklet 예시 4 - POJO 타입
     *
     * [실행 명령어]
     * <code>./gradlew bootRun --args='--spring.batch.job.name=processTerminatorJob missionName=안산_데이터센터_침투,java.lang.String operationCommander=KILL-9 securityLevel=3,java.lang.Integer,false'</code>
     */
    @Bean
    public Tasklet terminatorTasklet4(SystemInfiltrationParameters infiltrationParams) {
        return (contribution, chunkContext) -> {
            log.info("⚔️ 시스템 침투 작전 초기화!");
            log.info("임무 코드네임: {}", infiltrationParams.getMissionName());
            log.info("보안 레벨: {}", infiltrationParams.getSecurityLevel());
            log.info("작전 지휘관: {}", infiltrationParams.getOperationCommander());

            // 보안 레벨에 따른 침투 난이도 계산
            int baseInfiltrationTime = 60; // 기본 침투 시간 (분)
            int infiltrationMultiplier = switch (infiltrationParams.getSecurityLevel()) {
                case 1 -> 1; // 저보안
                case 2 -> 2; // 중보안
                case 3 -> 4; // 고보안
                case 4 -> 8; // 최고 보안
                default -> 1;
            };

            int totalInfiltrationTime = baseInfiltrationTime * infiltrationMultiplier;

            log.info("💥 시스템 해킹 난이도 분석 중...");
            log.info("🕒 예상 침투 시간: {}분", totalInfiltrationTime);
            log.info("🏆 시스템 장악 준비 완료!");

            return RepeatStatus.FINISHED;
        };
    }

    /**
     * JobParameter를 주입받는 Tasklet 예시 5 - 콤마(,)로 구분된 문자열 타입
     *
     * [실행 명령어]
     * <code>./gradlew bootRun --args="--spring.batch.job.name=processTerminatorJob infiltrationTargets='{\"value\":\"판교서버실,안산데이터센터\",\"type\":\"java.lang.String\"}'"</code>
     */
    @Bean
    @StepScope
    public Tasklet terminatorTasklet5(
            @Value("#{jobParameters['infiltrationTargets']}") String infiltrationTargets
    ) {
        return (contribution, chunkContext) -> {
            String[] targets = infiltrationTargets.split(",");

            log.info("⚡ 침투 작전 개시");
            log.info("첫 번째 타겟: {} 침투 시작", targets[0]);
            log.info("마지막 타겟: {} 에서 집결", targets[1]);
            log.info("🎯 임무 전달 완료");

            return RepeatStatus.FINISHED;
        };
    }

    /**
     * JSON 형식의 JobParameter 를 변환하기 위한 변환기 등록
     */
    @Bean
    public JobParametersConverter jobParametersConverter() {
        return new JsonJobParametersConverter();
    }
}
