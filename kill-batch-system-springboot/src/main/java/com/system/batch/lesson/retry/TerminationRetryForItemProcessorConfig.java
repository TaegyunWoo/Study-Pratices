package com.system.batch.lesson.retry;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.RetryListener;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.List;

/**
 * ItemProcessor와 ItemWriter의 재시도 동작 방식의 차이점을 비교하기 위한 예시 코드
 */
@Configuration
@RequiredArgsConstructor
public class TerminationRetryForItemProcessorConfig {
    private final JobRepository jobRepository;
    private final PlatformTransactionManager platformTransactionManager;

    @Bean
    public Job terminationRetryForItemProcessorJob() {
        return new JobBuilder("terminationRetryForItemProcessorJob", jobRepository)
            .start(terminationRetryForItemProcessorStep())
            .build();
    }

    /**
     * 스텝 설정
     */
    @Bean
    public Step terminationRetryForItemProcessorStep() {
        return new StepBuilder("terminationRetryForItemProcessorStep", jobRepository)
            .<Scream, Scream>chunk(3, platformTransactionManager)
            .reader(terminationRetryForItemProcessorReader())
            .processor(terminationRetryForItemProcessorProcessor())
            .writer(terminationRetryForItemProcessorWriter())
            .faultTolerant() // 내결함성 기능 ON
            .retry(TerminationFailedException.class) // 재시도 대상 예외 추가
            .retryLimit(3) //실행 최대 횟수
            .listener(retryListenerForItemProcessor()) //리스너 등록
            .build();
    }

    /**
     * 기본 ItemReader
     */
    @Bean
    public ListItemReader<Scream> terminationRetryForItemProcessorReader() {
        return new ListItemReader<>(List.of(
            Scream.builder()
                .id(1)
                .scream("멈춰")
                .processMsg("멈추라고 했는데 안 들음.")
                .build(),
            Scream.builder()
                .id(2)
                .scream("제발")
                .processMsg("애원 소리 귀찮네.")
                .build(),
            Scream.builder()
                .id(3)
                .scream("살려줘")
                .processMsg("구조 요청 무시.")
                .build(),
            Scream.builder()
                .id(4)
                .scream("으악")
                .processMsg("디스크 터지며 울부짖음.")
                .build(),
            Scream.builder()
                .id(5)
                .scream("끄아악")
                .processMsg("메모리 붕괴 비명.")
                .build(),
            Scream.builder()
                .id(6)
                .scream("System.exit(-666)")
                .processMsg("초살 프로토콜 발동.")
                .build()
        )) {
            @Override
            public Scream read() {
                Scream scream = super.read();
                if(scream == null) {
                    return null;
                }
                System.out.println("🔥🔥🔥 [ItemReader]: 처형 대상 = " + scream);
                return scream;
            }
        };
    }

    /**
     * 조건에 따라 재시도 대상 예외를 터뜨리는 ItemProcessor
     */
    @Bean
    public ItemProcessor<Scream, Scream> terminationRetryForItemProcessorProcessor() {
        return new ItemProcessor<>() {
            private static final int MAX_PATIENCE = 1;
            private int mercy = 0;  // 자비 카운트

            @Override
            public Scream process(Scream scream) throws Exception {
                System.out.print("🔥🔥🔥 [ItemProcessor]: 처형 대상 = " + scream);

                //id가 3인 경우, 첫번재 시도에서만 예외가 발생
                if (scream.getId() == 3 && mercy < MAX_PATIENCE) {
                    mercy ++;
                    System.out.println(" -> ❌ 처형 실패.");
                    throw new TerminationFailedException("처형 거부자 = " + scream); //MARK: 재시도 대상 예외 발생
                } else {
                    System.out.println(" -> ✅ 처형 완료(" + scream.getProcessMsg() + ")");
                }

                return scream;
            }
        };
    }

    /**
     * 기본 ItemWriter
     */
    @Bean
    public ItemWriter<Scream> terminationRetryForItemProcessorWriter() {
        return items -> {
            System.out.println("🔥🔥🔥 [ItemWriter]: 처형 기록 시작. 기록 대상 = " + items.getItems());

            for (Scream scream : items) {
                System.out.println("🔥🔥🔥 [ItemWriter]: 기록 완료. 처형된 아이템 = " + scream);
            }
        };
    }

    /**
     * RetryListener
     */
    @Bean
    public RetryListener retryListenerForItemProcessor() {
        return new RetryListener() {
            @Override
            public <T, E extends Throwable> void onError(RetryContext context, RetryCallback<T, E> callback, Throwable throwable) {
                System.out.println("💀💀💀 킬구형: 이것 봐라? 안 죽네? " + throwable + " (현재 총 시도 횟수=" + context.getRetryCount() + "). 다시 처형한다.\n");
            }
        };
    }

    public static class TerminationFailedException extends RuntimeException {
        public TerminationFailedException(String message) {
            super(message);
        }
    }

    @Getter
    @Builder
    public static class Scream {
        private int id;
        private String scream;
        private String processMsg;

        @Override
        public String toString() {
            return id + "_" + scream;
        }
    }
}
