package com.system.batch.lesson.flatfileitemreader;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.mapping.PatternMatchingCompositeLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.batch.item.file.transform.LineTokenizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.validation.BindException;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
//@Configuration
public class PatternMatchingSystemFailureJobConfig {
    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;

    @Bean
    public Job patternMatchingSystemLogJob(Step patternMatchingSystemLogStep) {
        return new JobBuilder("patternMatchingSystemLogJob", jobRepository)
            .start(patternMatchingSystemLogStep)
            .build();
    }

    @Bean
    public Step patternMatchingSystemLogStep(
        FlatFileItemReader<PatternMatchingSystemLog> systemLogReader,
        ItemWriter<PatternMatchingSystemLog> systemLogWriter
    ) {
        return new StepBuilder("patternMatchingSystemLogStep", jobRepository)
            .<PatternMatchingSystemLog, PatternMatchingSystemLog>chunk(10, transactionManager)
            .reader(systemLogReader)
            .writer(systemLogWriter)
            .build();
    }

    /**
     * ItemReader 정의
     */
    @Bean
    @StepScope
    public FlatFileItemReader<PatternMatchingSystemLog> patternMatchingSystemLogItemReader(
        @Value("#{jobParameters['inputFile']}") String inputFile
    ) {
        return new FlatFileItemReaderBuilder<PatternMatchingSystemLog>()
            .name("patternMatchingSystemLogReader")
            .resource(new FileSystemResource(inputFile))
            .lineMapper(patternMatchingSystemLogLineMapper())
            .build();
    }

    /**
     * 라인별 tokenizer 와 fieldSetMapper 설정
     */
    @Bean
    public PatternMatchingCompositeLineMapper patternMatchingSystemLogLineMapper() {
        PatternMatchingCompositeLineMapper<PatternMatchingSystemLog> lineMapper = new PatternMatchingCompositeLineMapper<>();

        //Custom Tokenizers
        Map<String, LineTokenizer> tokenizers = new HashMap<>();
        tokenizers.put("ERROR*", patternMatchingErrorLineTokenizer()); //라인의 문자열과 매칭할 패턴을 Key로 설정
        tokenizers.put("ABORT*", patternMatchingAbortLineTokenizer()); //라인의 문자열과 매칭할 패턴을 Key로 설정
        tokenizers.put("COLLECT*", patternMatchingCollectLineTokenizer()); //라인의 문자열과 매칭할 패턴을 Key로 설정
        lineMapper.setTokenizers(tokenizers); //커스텀 토크나이저 설정

        //Custom FieldSetMapper
        Map<String, FieldSetMapper<PatternMatchingSystemLog>> mappers = new HashMap<>();
        mappers.put("ERROR*", new ErrorFieldSetMapper()); //라인의 문자열과 매칭할 패턴을 Key로 설정
        mappers.put("ABORT*", new AbortFieldSetMapper()); //라인의 문자열과 매칭할 패턴을 Key로 설정
        mappers.put("COLLECT*", new CollectFieldSetMapper()); //라인의 문자열과 매칭할 패턴을 Key로 설정
        lineMapper.setFieldSetMappers(mappers); //커스텀 매퍼 설정

        return lineMapper;
    }

    /**
     * ErrorLog 와 관련된 라인을 쪼개는 Tokenizer
     */
    @Bean
    public DelimitedLineTokenizer patternMatchingErrorLineTokenizer() {
        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer(","); //쉼표를 구분자로 사용
        tokenizer.setNames("type", "application", "errorType", "timestamp", "message", "resourceUsage", "logPath");
        return tokenizer;
    }

    /**
     * AbortLog 와 관련된 라인을 쪼개는 Tokenizer
     */
    @Bean
    public DelimitedLineTokenizer patternMatchingAbortLineTokenizer() {
        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer(","); //쉼표를 구분자로 사용
        tokenizer.setNames("type", "application", "errorType", "timestamp", "message", "exitCode", "processPath", "status");
        return tokenizer;
    }

    /**
     * CollectLog 와 관련된 라인을 쪼개는 Tokenizer
     */
    @Bean
    public DelimitedLineTokenizer patternMatchingCollectLineTokenizer() {
        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer(","); //쉼표를 구분자로 사용
        tokenizer.setNames("type", "dumpType", "processId", "timestamp", "dumpPath");
        return tokenizer;
    }

    /**
     * 단순히 로깅만하는 ItemWriter
     */
    @Bean
    public ItemWriter<PatternMatchingSystemLog> patternMatchingSystemLogWriter() {
        return items -> {
            for (PatternMatchingSystemLog patternMatchingSystemLog : items) {
                log.info("{}", patternMatchingSystemLog);
            }
        };
    }

    @Data
    public static class PatternMatchingSystemLog {
        private String type;
        private String timestamp;
    }

    @Data
    @ToString(callSuper = true)
    public static class ErrorLogPatternMatching extends PatternMatchingSystemLog {
        private String application;
        private String errorType;
        private String message;
        private String resourceUsage;
        private String logPath;
    }

    @Data
    @ToString(callSuper = true)
    public static class AbortLogPatternMatching extends PatternMatchingSystemLog {
        private String application;
        private String errorType;
        private String message;
        private String exitCode;
        private String processPath;
        private String status;
    }

    @Data
    @ToString(callSuper = true)
    public static class CollectLogPatternMatching extends PatternMatchingSystemLog {
        private String dumpType;
        private String processId;
        private String dumpPath;
    }

    /**
     * ErrorLog 객체에 필드값을 매핑하는 커스텀 FieldSetMapper
     */
    public static class ErrorFieldSetMapper implements FieldSetMapper<PatternMatchingSystemLog> {
        @Override
        public PatternMatchingSystemLog mapFieldSet(FieldSet fieldSet) throws BindException {
            ErrorLogPatternMatching errorLog = new ErrorLogPatternMatching();
            errorLog.setType(fieldSet.readString("type"));
            errorLog.setApplication(fieldSet.readString("application"));
            errorLog.setErrorType(fieldSet.readString("errorType"));
            errorLog.setTimestamp(fieldSet.readString("timestamp"));
            errorLog.setMessage(fieldSet.readString("message"));
            errorLog.setResourceUsage(fieldSet.readString("resourceUsage"));
            errorLog.setLogPath(fieldSet.readString("logPath"));
            return errorLog;
        }
    }

    /**
     * AbortLog 객체에 필드값을 매핑하는 커스텀 FieldSetMapper
     */
    public static class AbortFieldSetMapper implements FieldSetMapper<PatternMatchingSystemLog> {
        @Override
        public PatternMatchingSystemLog mapFieldSet(FieldSet fs) throws BindException {
            AbortLogPatternMatching abortLog = new AbortLogPatternMatching();
            abortLog.setType(fs.readString("type"));
            abortLog.setApplication(fs.readString("application"));
            abortLog.setErrorType(fs.readString("errorType"));
            abortLog.setTimestamp(fs.readString("timestamp"));
            abortLog.setMessage(fs.readString("message"));
            abortLog.setExitCode(fs.readString("exitCode"));
            abortLog.setProcessPath(fs.readString("processPath"));
            abortLog.setStatus(fs.readString("status"));
            return abortLog;
        }
    }

    /**
     * CollectLog 객체에 필드값을 매핑하는 커스텀 FieldSetMapper
     */
    public static class CollectFieldSetMapper implements FieldSetMapper<PatternMatchingSystemLog> {
        @Override
        public PatternMatchingSystemLog mapFieldSet(FieldSet fs) throws BindException {
            CollectLogPatternMatching collectLog = new CollectLogPatternMatching();
            collectLog.setType(fs.readString("type"));
            collectLog.setDumpType(fs.readString("dumpType"));
            collectLog.setProcessId(fs.readString("processId"));
            collectLog.setTimestamp(fs.readString("timestamp"));
            collectLog.setDumpPath(fs.readString("dumpPath"));
            return collectLog;
        }
    }
}
