package practice.testCode;

import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.test.context.event.annotation.AfterTestExecution;
import org.springframework.test.context.event.annotation.BeforeTestExecution;

public class FindSlowExtension implements BeforeTestExecutionCallback, AfterTestExecutionCallback {

    private static final long THRESHOLD = 1000L;

    //테스트 메서드 호출 후, 호출되는 메서드
    @Override
    public void afterTestExecution(ExtensionContext context) throws Exception {
        String testClassName = context.getRequiredTestClass().getName();
        String testMethodName = context.getRequiredTestMethod().getName();

        //확장 모델 내부에서 공유할 수 있는 저장소(Store) 가져오기
        ExtensionContext.Store store = context.getStore(ExtensionContext.Namespace.create(testClassName, testMethodName));

        //확장 모델 내부에서 공유할 수 있는 저장소(Store)에 저장된 값 삭제 및 가져오기
        Long start_time = store.remove("START_TIME", long.class);
        long duration = System.currentTimeMillis() - start_time;

        if (duration > THRESHOLD) {
            System.out.printf("please consider mark method [%s] with @SlowTest.\n", testMethodName);
        }

    }

    //테스트 메서드 호출 전, 호출되는 메서드
    @Override
    public void beforeTestExecution(ExtensionContext context) throws Exception {
        String testClassName = context.getRequiredTestClass().getName();
        String testMethodName = context.getRequiredTestMethod().getName();

        //확장 모델 내부에서 공유할 수 있는 저장소(Store) 가져오기
        ExtensionContext.Store store = context.getStore(ExtensionContext.Namespace.create(testClassName, testMethodName));

        //확장 모델 내부에서 공유할 수 있는 저장소(Store)에 값 저장하기
        store.put("START_TIME", System.currentTimeMillis());
    }
}
