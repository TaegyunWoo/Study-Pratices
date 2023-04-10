package practice.testCode.member;



import practice.testCode.domain.Member;
import practice.testCode.domain.Study;

import java.util.Optional;

public interface MemberService {

    Optional<Member> findById(Long memberId);

    Member findMember(Long memberId);

    Member findButThrow(Long memberId);

    void validate(Long memberId);

    void notify(Study newstudy);

//    void notify(Member member);
}
