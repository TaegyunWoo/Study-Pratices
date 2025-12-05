package com.system.batch.lesson.flatfileitemwriter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.SystemCommandTasklet;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.MultiResourceItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.transaction.PlatformTransactionManager;

import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.StringJoiner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Configuration
@RequiredArgsConstructor
public class LogProcessingJobConfig {
    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;

    /**
     * 잡 정의
     */
    @Bean
    public Job logProcessingJob(
        Step createDirectoryStep,
        Step logCollectionStep,
        Step logProcessingStep
    ) {
        return new JobBuilder("logProcessingJob", jobRepository)
            .start(createDirectoryStep) //디렉토리 생성 스텝
            .next(logCollectionStep) //로그 수집 스텝
            .next(logProcessingStep) //로그 처리 스텝
            .build();
    }

    /**
     * 디렉토리 생성 스텝 정의
     */
    @Bean
    public Step createDirectoryStep(SystemCommandTasklet mkdirTasklet) {
        return new StepBuilder("createDirectoryStep", jobRepository)
            .tasklet(mkdirTasklet, transactionManager)
            .build();
    }

    /**
     * 디렉토리 생성 Tasklet 정의
     */
    @Bean
    @StepScope
    public SystemCommandTasklet mkdirTasklet(
        @Value("#{jobParameters['date']}") String date
    ) {
        SystemCommandTasklet tasklet = new SystemCommandTasklet(); //외부 스크립트 실행 혹은 파일 시스템 작업을 위한 테스크릿
        tasklet.setWorkingDirectory(System.getProperty("user.home"));

        String collectedLogsPath = "collected_ecommerce_logs/" + date;
        String processedLogsPath = "processed_logs/" + date;

        tasklet.setCommand("mkdir", "-p", collectedLogsPath, processedLogsPath, " && ls -al"); //실행할 명령어 설정
        tasklet.setTimeout(3000); // 3초 타임아웃
        return tasklet;
    }


    /**
     * 로그 수집 스텝 정의
     */
    @Bean
    public Step logCollectionStep(SystemCommandTasklet scpTasklet) {
        return new StepBuilder("logCollectionStep", jobRepository)
            .tasklet(scpTasklet, transactionManager)
            .build();
    }

    /**
     * 로그 수집 Tasklet 정의
     */
    @Bean
    @StepScope
    public SystemCommandTasklet scpTasklet(
        @Value("#{jobParameters['date']}") String date
    ) {
        SystemCommandTasklet tasklet = new SystemCommandTasklet(); //외부 스크립트 실행 혹은 파일 시스템 작업을 위한 테스크릿
        tasklet.setWorkingDirectory(System.getProperty("user.home"));
        String processedLogsPath = "collected_ecommerce_logs/" + date;

        StringJoiner commandBuilder = new StringJoiner(" && ");
        for (String host : List.of("localhost")) {
            String command = String.format("scp %s:~/ecommerce_logs/%s.log ./%s/%s.log", //실행할 명령어 정의 (scp 명령으로 각 서버로부터 로그 파일을 가져온다.)
                host, date, processedLogsPath, host);
            commandBuilder.add(command);
        }

        tasklet.setCommand("/bin/sh", "-c", commandBuilder.toString());
        tasklet.setTimeout(10000); //10초 타임아웃
        return tasklet;
    }

    /**
     * 로그 처리 스텝 정의
     */
    @Bean
    public Step logProcessingStep(
        MultiResourceItemReader<LogEntry> multiResourceItemLogReader,
        LogEntryProcessor logEntryProcessor,
        FlatFileItemWriter<ProcessedLogEntry> processedLogEntryJsonWriter
    ) {
        return new StepBuilder("logProcessingStep", jobRepository)
            .<LogEntry, ProcessedLogEntry>chunk(10, transactionManager)
            .reader(multiResourceItemLogReader) //ItemReader 설정
            .processor(logEntryProcessor) //ItemProcessor 설정
            .writer(processedLogEntryJsonWriter) //ItemWriter 설정
            .build();
    }

    /**
     * 읽기 작업을 관리하는 ItemReader
     */
    @Bean
    @StepScope
    public MultiResourceItemReader<LogEntry> multiResourceItemLogReader(
        @Value("#{jobParameters['date']}") String date
    ) {
        MultiResourceItemReader<LogEntry> reader = new MultiResourceItemReader<>(); //여러 파일을 읽기 위한 구현체
        reader.setName("multiResourceItemLogReader");
        reader.setResources(getResources(date));
        reader.setDelegate(logFileReader()); //실제 읽기 작업은 위임
        return reader;
    }

    private Resource[] getResources(String date) {
        try {
            String userHome = System.getProperty("user.home");
            String location = "file:" + userHome + "/collected_ecommerce_logs/" + date + "/*.log";

            PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver(); //패턴에 맞는 리소스를 가져오기 위한 리졸버
            return resolver.getResources(location); //패턴에 맞는 모든 리소스를 반환
        } catch (IOException e) {
            throw new RuntimeException("Failed to resolve log files", e);
        }
    }

    /**
     * ItemReader
     */
    @Bean
    public FlatFileItemReader<LogEntry> logFileReader() {
        return new FlatFileItemReaderBuilder<LogEntry>()
            .name("logFileReader")
            .delimited()
            .delimiter(",")
            .names("dateTime", "level", "message")
            .targetType(LogEntry.class)
            .build();
    }

    /**
     * ItemProcessor
     */
    @Bean
    public LogEntryProcessor logEntryProcessor() {
        return new LogEntryProcessor();
    }

    @Bean
    @StepScope
    public FlatFileItemWriter<ProcessedLogEntry> processedLogEntryJsonWriter(
        @Value("#{jobParameters['date']}") String date
    ) {
        String userHome = System.getProperty("user.home");
        String outputPath = Paths.get(userHome, "processed_logs", date, "processed_logs.jsonl").toString();

        ObjectMapper objectMapper = new ObjectMapper();
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addSerializer(LocalDateTime.class,
            new LocalDateTimeSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")));
        objectMapper.registerModule(javaTimeModule);

        return new FlatFileItemWriterBuilder<ProcessedLogEntry>()
            .name("processedLogEntryJsonWriter")
            .resource(new FileSystemResource(outputPath))
            .lineAggregator(item -> { //LineAggregator(문자열 결합) 인터페이스의 구현체 직접 정의
                try {
                    return objectMapper.writeValueAsString(item);
                } catch (Exception e) {
                    throw new RuntimeException("Error converting item to JSON", e);
                }
            })
            .build();
    }

    @Data
    public static class LogEntry {
        private String dateTime;
        private String level;
        private String message;
    }

    @Data
    public static class ProcessedLogEntry {
        private LocalDateTime dateTime;
        private LogLevel level;
        private String message;
        private String errorCode;
    }

    public enum LogLevel {
        INFO, WARN, ERROR, DEBUG, UNKNOWN;

        public static LogLevel fromString(String level) {
            if (level == null || level.trim().isEmpty()) {
                return UNKNOWN;
            }
            try {
                return valueOf(level.toUpperCase());
            } catch (IllegalArgumentException e) {
                return UNKNOWN;
            }
        }
    }

    /**
     * ItemReader 가 읽은 데이터를 가공한다.
     * - 가공전 : LogEntry
     * - 가공후 : ProcessedLogEntry
     */
    public static class LogEntryProcessor implements ItemProcessor<LogEntry, ProcessedLogEntry> {
        private static final DateTimeFormatter ISO_FORMATTER = DateTimeFormatter.ISO_DATE_TIME;
        private static final Pattern ERROR_CODE_PATTERN = Pattern.compile("ERROR_CODE\\[(\\w+)]");

        @Override
        public ProcessedLogEntry process(LogEntry item) {
            ProcessedLogEntry processedEntry = new ProcessedLogEntry();
            processedEntry.setDateTime(parseDateTime(item.getDateTime()));
            processedEntry.setLevel(parseLevel(item.getLevel()));
            processedEntry.setMessage(item.getMessage());
            processedEntry.setErrorCode(extractErrorCode(item.getMessage()));
            return processedEntry;
        }

        private LocalDateTime parseDateTime(String dateTime) {
            return LocalDateTime.parse(dateTime, ISO_FORMATTER);
        }

        private LogLevel parseLevel(String level) {
            return LogLevel.fromString(level);
        }

        private String extractErrorCode(String message) {
            if (message == null) {
                return null;
            }

            Matcher matcher = ERROR_CODE_PATTERN.matcher(message);
            if (matcher.find()) {
                return matcher.group(1);
            }
            // ERROR 문자열이 포함되어 있지만 패턴이 일치하지 않는 경우
            if (message.contains("ERROR")) {
                return "UNKNOWN_ERROR";
            }
            return null;
        }
    }
}
