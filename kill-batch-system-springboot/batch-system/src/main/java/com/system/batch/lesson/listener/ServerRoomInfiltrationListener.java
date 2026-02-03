package com.system.batch.lesson.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.annotation.AfterJob;
import org.springframework.batch.core.annotation.BeforeJob;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ServerRoomInfiltrationListener {
    @BeforeJob
    public void infiltrateServerRoom(JobExecution jobExecution) {
        log.info("판교 서버실 침투 시작. 보안 시스템 무력화 진행중.");
    }

    @AfterJob
    public void escapeServerRoom(JobExecution jobExecution) {
        log.info("파괴 완료. 침투 결과: {}", jobExecution.getStatus());
    }
}
