package practice.testCode;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.OS;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.aggregator.AggregateWith;
import org.junit.jupiter.params.aggregator.ArgumentsAccessor;
import org.junit.jupiter.params.aggregator.ArgumentsAggregationException;
import org.junit.jupiter.params.aggregator.ArgumentsAggregator;
import org.junit.jupiter.params.converter.ArgumentConversionException;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.converter.SimpleArgumentConverter;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.SimpleTypeConverter;

import java.time.Duration;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.*;

/**
 * 1초가 넘게 걸리는 테스트 메서드의 경우, @SlowTest를 붙이도록 권장하는 확장 모델
 */
@ExtendWith(FindSlowExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class StudyTest {

    @Test
    @DisplayName("Study 객체 생성 테스트")
    @EnabledOnOs(OS.WINDOWS)
    @Order(1)
    @Tag("myTag")
    void create() {
        //true인 경우, 본 테스트 메서드 수행
        assumeTrue(true);

        try {
            Thread.sleep(1050);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("StudyTest.create\n");
        Study study = new Study(10);

        assertAll(
                () -> assertNotNull(study),
                () -> assertEquals(StudyStatus.DRAFT, study.getStudyStatus(), new Supplier<String>() {
                    @Override
                    public String get() {
                        return "study 객체 생성시, 상태는 DRAFT이어야 한다.";
                    }
                }),
                () -> assertTrue(study.getLimit() > 0, "study 객체의 limit 값은 0보다 커야한다."),
                () -> assertThrows(IllegalArgumentException.class, () -> new Study(-10)),
                () -> assertTimeout(Duration.ofMillis(100), () -> {
                    Thread.sleep(50);
                }),
                () -> assertTimeoutPreemptively(Duration.ofMillis(100), () -> {
                    Thread.sleep(10);
                })
        );
    }

    @Disabled
    @Order(2)
    @FastTest
    void create1() {
        System.out.println("StudyTest.create1\n");
        Study study = new Study();
        assertNotNull(study);
    }

    @DisplayName("테스트 반복")
    @Order(3)
    @RepeatedTest(name = "{displayName}: 전체 {totalRepetitions} 중 {currentRepetition}번", value = 5)
    void repeatTest(RepetitionInfo info) {
        System.out.println("StudyTest.repeatTest" + info.getCurrentRepetition());
    }

    @DisplayName("매개변수 반복")
    @Order(4)
    @ParameterizedTest(name = "{index} {displayName} message={0}")
    @ValueSource(strings = {"A", "B", "C"})
    @NullSource
    void paramRepeatRest(String s) {
        System.out.println("s = " + s);
    }

    @DisplayName("커스텀 타입 컨버터 + 매개변수 반복 1")
    @Order(5)
    @ParameterizedTest(name = "{displayName}: 현재반복횟수={index}, 전달받은 매개변수값={0}")
    @ValueSource(ints = {10, 20, 40})
    void paramRepeatWithCustomTypeConvert(@ConvertWith(StudyConverter.class) Study study) {
        System.out.println("study.getLimit() = " + study.getLimit());
    }

    @DisplayName("커스텀 타입 컨버터 + 매개변수 반복 2")
    @ParameterizedTest(name = "{displayName} 현재반복횟수={index}, 전달받은 매개변수값={0}")
    @ValueSource(strings = {"A", "B", "C"})
    void paramRepeat(String s) {
        System.out.println("s = " + s);
    }

    @DisplayName("커스텀 타입 컨버터 + 매개변수 반복 3")
    @Order(6)
    @ParameterizedTest
    @CsvSource({"10, '자바'", "20, '공부'"})
    void paramRepeatWithCsv1(int i, String s) {
        System.out.println("i = " + i + ", s = " + s);
    }

    @DisplayName("커스텀 타입 컨버터 + 매개변수 반복 4")
    @Order(7)
    @ParameterizedTest
    @CsvSource({"10, '자바'", "20, '공부'"})
    void paramRepeatWithCsv2(ArgumentsAccessor argumentsAccessor) {
        Study study = new Study(argumentsAccessor.getInteger(0), argumentsAccessor.getString(1));
        System.out.println("study = " + study);
    }

    @DisplayName("커스텀 타입 컨버터 + 매개변수 반복 5")
    @Order(8)
    @ParameterizedTest
    @CsvSource({"10, '자바'", "20, '공부'"})
    void paramRepeatWithCsv3(@AggregateWith(StudyAggregator.class) Study study) {
        System.out.println("study = " + study);
    }

    @Test
    void test() {
        assumingThat(false, () -> {
            System.out.println("StudyTest.test");
        });
    }

//    @BeforeAll
//    static void beforeAll() {
//        System.out.println("StudyTest.beforeAll");
//    }
//
//    @AfterAll
//    static void afterAll() {
//        System.out.println("StudyTest.afterAll");
//    }
//
//    @BeforeEach
//    void beforeEach() {
//        System.out.println("StudyTest.beforeEach");
//    }
//
//    @AfterEach
//    void afterEach() {
//        System.out.println("StudyTest.afterEach");
//    }

    static class StudyConverter extends SimpleArgumentConverter {
        @Override
        protected Object convert(Object source, Class<?> targetType) throws ArgumentConversionException {
            Study study = new Study(Integer.parseInt(source.toString()));
            return study;
        }
    }

    static class StudyAggregator implements ArgumentsAggregator {
        @Override
        public Object aggregateArguments(ArgumentsAccessor accessor, ParameterContext context) throws ArgumentsAggregationException {
            Study study = new Study(accessor.getInteger(0), accessor.getString(1));
            return study;
        }
    }


}