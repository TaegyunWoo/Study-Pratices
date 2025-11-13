package com.system.batch.lesson.jobparam;

import lombok.Data;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Pojo 클래스에 JobParameter 주입하는 예시
 */
@Data
@StepScope
@Component
public class SystemInfiltrationParameters {
    /**
     * JobParameter 필드 주입
     */
    @Value("#{jobParameters['missionName']}")
    private String missionName;
    private int securityLevel;
    private final String operationCommander;

    /**
     * JobParameter 생성자 주입
     */
    public SystemInfiltrationParameters(
        @Value("#{jobParameters['operationCommander']}")
        String operationCommander
    ) {
        this.operationCommander = operationCommander;
    }

    /**
     * JobParameter 세터 주입
     */
    @Value("#{jobParameters['securityLevel']}")
    public void setSecurityLevel(int securityLevel) {
        this.securityLevel = securityLevel;
    }
}
