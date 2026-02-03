package com.system.batch.lesson.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.AfterStep;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ServerRackControlListener {
    @BeforeStep
    public void accessServerRack(StepExecution stepExecution) {
        log.info("서버랙 접근 시작. 콘센트를 찾는 중.");
    }

    @AfterStep
    public ExitStatus leaveServerRack(StepExecution stepExecution) {
        log.info("코드를 뽑아버렸다.");
        return new ExitStatus("POWER_DOWN"); // @AfterStep 사용시 반환 타입을 반드시 ExitStatus로 맞춰야 한다.
    }
}
