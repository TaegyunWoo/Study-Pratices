package com.system.batch.tasklet;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

@Slf4j
public class ZombieProcessCleanupTasklet implements Tasklet {
    private final int processesToKill = 10;
    private int killedProcesses = 0;

    /**
     * return 값에 따라서 작업이 계속될지 혹은 종료될지가 결정된다.
     * 단순 반복문이 아니라, Spring Batch의 Tasklet 구조를 따르기 때문에
     * execute 메서드가 호출될 때마다 하나의 트랜잭션 단위로 처리된다.
     *
     * - 단순 반복문으로 처리 : 거대한 하나의 트랜잭션
     * - Tasklet의 execute 메서드로 처리 : 여러 개의 작은 트랜잭션
     * @param contribution
     * @param chunkContext
     * @return
     * @throws Exception
     */
    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        killedProcesses++;
        log.info("☠️  프로세스 강제 종료... ({}/{})", killedProcesses, processesToKill);

        if (killedProcesses >= processesToKill) {
            log.info("☠️  시스템 안정화 완료. 모든 좀비 프로세스 제거.");
            return RepeatStatus.FINISHED; // 모든 프로세스 종료 후 작업 완료
        } else {
            return RepeatStatus.CONTINUABLE; // 아직 더 종료할 프로세스가 남음
        }
    }
}
