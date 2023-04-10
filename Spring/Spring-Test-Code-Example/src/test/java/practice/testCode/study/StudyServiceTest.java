package practice.testCode.study;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.junit.jupiter.MockitoExtension;
import practice.testCode.StudyStatus;
import practice.testCode.domain.Member;
import practice.testCode.domain.Study;
import practice.testCode.member.MemberService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudyServiceTest {

    /**
     * Mockito.mock() 으로 mock 객체 만들기
     */
    @Test
    void createStudyServiceWithMethod() {
        /*
         StudyService 생성자의 인수로
         'MemberService'객체와, 'StudyRepository' 객체를 넣어야하지만
         현재 인터페이스만 존재하는 상황이다.
         따라서, mock 객체를 통해 StudyService 객체를 생성할 수 있다.
         */
        MemberService memberService = mock(MemberService.class);
        StudyRepository studyRepository = mock(StudyRepository.class);

        StudyService studyService = new StudyService(memberService, studyRepository);
        assertNotNull(studyService);
    }

    /**
     * @Mock 애너테이션으로 mock 객체 만들기
     */
    @Test
    void createStudyServiceWithAnnotaion(@Mock MemberService memberServiceWithAnno,
                                         @Mock StudyRepository studyRepositoryWithAnno
                                         ) {

        /*
         StudyService 생성자의 인수로
         'MemberService'객체와, 'StudyRepository' 객체를 넣어야하지만
         현재 인터페이스만 존재하는 상황이다.
         따라서, mock 객체를 통해 StudyService 객체를 생성할 수 있다.
         */
        StudyService studyService = new StudyService(memberServiceWithAnno, studyRepositoryWithAnno);
        assertNotNull(studyService);

        //---------- mock 객체의 기본 행동 -----------
        //리턴값이 Optional인 경우: Optional.empty 반환
        Optional<Member> optional = memberServiceWithAnno.findById(1L);

        //리턴값이 Optional이 아닌 경우: null 반환
        Member member = memberServiceWithAnno.findMember(1L);

        //리턴값이 void인 경우: 아무일도 일어나지 않는다.
        memberServiceWithAnno.validate(1L);
    }

    @Test
    void createStudyServiceWithStubbing(@Mock MemberService memberService,
                                        @Mock StudyRepository repository) {
        StudyService studyService = new StudyService(memberService, repository);
        assertNotNull(studyService);

        //---------- mock 행동 정의 ----------
        Member member = new Member();
        member.setId(1L);
        member.setEmail("test@test.com");
        when(memberService.findMember(1L)).thenReturn(member); //findMember 메서드의 인수가 1L일 때만 동작
        //-------------------------------

        Member member1 = memberService.findMember(1L);
        assertEquals(member, member1);


    }

    /**
     * 총정리
     */
    @Test
    void createNewStudy(@Mock MemberService memberService,
                        @Mock StudyRepository repository) {
        StudyService studyService = new StudyService(memberService, repository);
        assertNotNull(studyService);

        //---------- mock 행동 정의 ----------
        Member member = new Member();
        member.setId(1L);
        member.setEmail("test@test.com");

        // [리턴 값이 있는 경우: 값 리턴하기]
        when(memberService.findById(1L)).thenReturn(Optional.of(member));
        when(memberService.findMember(any())).thenReturn(member); // any()로 파라미터를 전달하면, 어떤 매개변수라도 같은 값을 리턴한다.
        // [리턴 값이 있는 경우: 예외 던지기]
        when(memberService.findButThrow(1L)).thenThrow(new RuntimeException());
        assertThrows(RuntimeException.class, () -> {
            memberService.findButThrow(1L);
        });

        // [리턴 값이 없는 경우: 예외 던지기]
        doThrow(new RuntimeException()).when(memberService).validate(1L);
        assertThrows(RuntimeException.class, () -> {
            memberService.validate(1L);
        });

        // [여러번 호출시, 각각 다른 행동하기]
        when(memberService.findMember(5L)).thenReturn(member)                //첫 번째 호출시
                                                    .thenThrow(new RuntimeException()) //두 번째 호출시
                                                    .thenReturn(member);               //세 번째 호출시
        assertThrows(RuntimeException.class, () -> {
            memberService.findMember(5L);
            memberService.findMember(5L); //이때 예외 발생
            memberService.findMember(5L);
        });
        //-------------------------------------

        //--------- 직접 mock 객체의 메서드의 반환값 얻기 ---------
        Optional<Member> optionalMember = memberService.findById(1L);
        assertEquals("test@test.com", optionalMember.get().getEmail());

        //-----------------------------------


        Study study = new Study(10, "java");

        /*
         StudyService 의 createNewStudy() 메서드는
         MemberService 의 findById(memberId) 메서드를 호출한다.
         이때, 위에서 정의한 mock 객체의 행동을 수행한다.
         왜냐하면, StudyService 에 주입한 MemberService 객체가 mock 객체이기 때문이다.
         */
        studyService.createNewStudy(1L, study);
    }

    /**
     * mock 객체가 어떻게 사용됐는지 확인하기
     */
    @Test
    void checkMockInstance(@Mock MemberService memberService,
                           @Mock StudyRepository repository) {
        StudyService studyService = new StudyService(memberService, repository);
        assertNotNull(studyService);
        Member member = new Member();
        member.setId(1L);
        member.setEmail("test@test.com");

        when(memberService.findById(1L)).thenReturn(Optional.of(member));

        Study study = new Study(10, "java");
        Study newStudy = studyService.createNewStudy(1L, study);

        //----------- 지금까지 mock 객체의 메서드가 얼마나 호출됐는지 --------
        verify(memberService, times(1)).findById(1L);

        //---------------특정 시점 이후, mock객체를 사용하는지----------------
        verify(memberService).notify(newStudy); //notify()가 호출된 시점이 기준이 됨.
        verifyNoMoreInteractions(memberService); //notify()가 호출된 후 mock객체를 사용하는가
    }

    @Test
    void BddStyle(@Mock MemberService memberService,
                  @Mock StudyRepository repository) {
        StudyService studyService = new StudyService(memberService, repository);
        assertNotNull(studyService);
        Member member = new Member();
        member.setId(1L);
        member.setEmail("test@test.com");

        Study study = new Study(10, "java");

        //---------BDD 스타일 변경 (when -> given)---------
//        when(memberService.findById(1L)).thenReturn(Optional.of(member));
        given(memberService.findById(1L)).willReturn(Optional.of(member));

        Study newStudy = studyService.createNewStudy(1L, study);

        //---------BDD 스타일 변경 (verify -> then)---------
        //  verify(memberService, times(1)).findById(1L);
        then(memberService).should(times(1)).findById(1L);

        //  verify(memberService).notify(newStudy)
        //  verifyNoMoreInteractions(memberService);
        then(memberService).should().notify(newStudy);
        then(memberService).shouldHaveNoMoreInteractions();
    }

    @Test
    void test(@Mock MemberService memberService,
              @Mock StudyRepository studyRepository
    ) {
        StudyService studyService = new StudyService(memberService, studyRepository);
        Study study = new Study(10, "java");
        Member member = new Member();
        member.setId(1L);
        member.setEmail("test@test.com");

        //행동 정의
//        when(memberService.findById(1L)).thenReturn(Optional.of(member)); //findById 메서드의 인수가 1L일 때만 동작
//        when(memberService.findById(any())).thenReturn(Optional.of(member)); //findById 메서드의 인수에 상관없이 동작
//        when(memberService.findById(1L)).thenThrow(new RuntimeException()); //findById 메서드의 인수가 1L일 때만 RuntimeException 던지기
//        doThrow(new RuntimeException()).when(memberService).notify(any()); //notify 메서드의 인수에 상관없이 동작

        when(memberService.findById(1L)).thenReturn(Optional.of(member))
                                            .thenReturn(Optional.of(member))
                                            .thenThrow(new RuntimeException());

        studyService.createNewStudy(1L, study); //1번째 동작
        studyService.createNewStudy(1L, study); //2번째 동작
        studyService.createNewStudy(1L, study); //3번째 동작 => 예외발생
    }

    @DisplayName("행동 검증")
    @Test
    void testValidation(@Mock MemberService memberService,
                        @Mock StudyRepository studyRepository) {
        StudyService studyService = new StudyService(memberService, studyRepository);
        Study study = new Study(10, "java");
        Member member = new Member();
        member.setId(1L);
        member.setEmail("test@test.com");

        // ----------- 행동 정의 -----------
        when(memberService.findById(1L)).thenReturn(Optional.of(member));
        when(studyRepository.save(study)).thenReturn(study);

        // ----------- 행동 호출 -----------
        Study newStudy = studyService.createNewStudy(1L, study); //1번만 호출됨

        // ----------- 행동 검증 -----------
//        verify(memberService, times(1)).findById(1L); //횟수 검증
        verify(memberService).findById(any()); //기준 시점 = notify()가 호출된 시점
        verifyNoMoreInteractions(memberService); //기준 시점 이후에, memberService 객체(mock)가 사용되지 않았나?

        // ----------- 테스트 검증 -----------
        assertEquals(study, newStudy);
    }

    @DisplayName("BDD 스타일 API")
    @Test
    void testBdd(@Mock MemberService memberService,
                 @Mock StudyRepository studyRepository) {
        //------------- given -------------
        StudyService studyService = new StudyService(memberService, studyRepository);
        Study study = new Study(10, "java");
        Member member = new Member();
        member.setId(1L);
        member.setEmail("test@test.com");

        //행동 정의(given)
        given(memberService.findById(1L)).willReturn(Optional.of(member));
        given(studyRepository.save(study)).willReturn(study);

        //------------- when -------------
        Study newStudy = studyService.createNewStudy(1L, study);

        //------------- then -------------
        assertEquals(study, newStudy);

        //행동 검증
        then(memberService).should(times(1)).findById(1L);
    }

}