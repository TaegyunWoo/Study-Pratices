package com.system.batch.mvc.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@RestController
@RequestMapping("v2/api/jobs")
@RequiredArgsConstructor
public class JobOperatorController {
    private final JobOperator jobOperator;
    private final JobExplorer jobExplorer;

    /**
     * Job 실행
     */
    @PostMapping("/{jobName}/start")
    public ResponseEntity<String> launchJob(
        @PathVariable String jobName
    ) throws Exception {
        //매번 다른 JobParameters 로 실행하기 위해 타임스탬프를 넣어준다.
        Properties jobParameters = new Properties();
        jobParameters.setProperty("run.timestamp", String.valueOf(System.currentTimeMillis()));

        Long executionId = jobOperator.start(jobName, jobParameters);
        return ResponseEntity.ok("Job launched with ID: " + executionId);
    }

    /**
     * Job 실행 이력 조회
     * 특정 Job의 최근 실행 이력을 조회한다.
     * 이를 통해 Job의 실행 상태를 모니터링할 수도 있다.
     */
    @GetMapping("/{jobName}/executions")
    public ResponseEntity<List<String>> getJobExecutions(@PathVariable String jobName) {
        List<JobInstance> jobInstances = jobExplorer.getJobInstances(jobName, 0, 10);
        List<String> executionInfo = new ArrayList<>();

        for (JobInstance jobInstance : jobInstances) {
            List<JobExecution> executions = jobExplorer.getJobExecutions(jobInstance);
            for (JobExecution execution : executions) {
                executionInfo.add(String.format("Execution ID: %d, Status: %s",
                    execution.getId(), execution.getStatus()));
            }
        }

        return ResponseEntity.ok(executionInfo);
    }

    /**
     * Job 실행 중지
     * 반환값으로 중지 요청이 성공적으로 전달되었는지만 나타낸다.
     */
    @PostMapping("/stop/{executionId}")
    public ResponseEntity<String> stopJob(@PathVariable Long executionId) throws Exception {
        boolean stopped = jobOperator.stop(executionId);
        return ResponseEntity.ok("Stop request for job execution " + executionId +
            (stopped ? " successful" : " failed"));
    }

    /**
     * 중지되거나 실패한 Job을 재시작한다.
     */
    @PostMapping("/restart/{executionId}")
    public ResponseEntity<String> restartJob(@PathVariable Long executionId) throws Exception {
        Long newExecutionId = jobOperator.restart(executionId);
        return ResponseEntity.ok("Job restarted with new execution ID: " + newExecutionId);
    }
}
