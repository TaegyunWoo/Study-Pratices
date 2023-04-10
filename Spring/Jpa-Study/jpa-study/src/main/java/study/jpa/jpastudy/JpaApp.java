package study.jpa.jpastudy;

import study.jpa.jpastudy.domain.Member;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class JpaApp {
    public static void main(String[] args) {

        // [엔티티 매니저 팩토리] - 생성
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpabook");
        System.out.println("Sadasdas!!!!!!!!!!!!!!!!!!!!!!!!!");


        // [엔티티 매니저] - 생성
        EntityManager em = emf.createEntityManager();

        // [트랜잭션] - 획득
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin(); // [트랜잭션] - 시작
            logic(em); // 비즈니스 로직 실행
            tx.commit(); // [트랜잭션] - 커밋
        } catch (Exception e) {
            tx.rollback(); // [트랜잭션] - 롤백
        } finally {
            em.close(); // [엔티티 매니저] - 종료
        }
        emf.close(); // [엔티티 매니저 팩토리] - 종료

    }

    private static void logic(EntityManager em) {
        String id = "id1";
        Member member = new Member();
        member.setId(id);
        member.setUsername("태균");
        member.setAge(2);

        //등록 - Create
        em.persist(member);

        //수정 - Update
        member.setAge(20);

        //한 건 조회 - Read
        Member findMember = em.find(Member.class, id);
        System.out.println(findMember);

        //목록 조회 - Read
        List<Member> memberList = em.createQuery("select m from Member m", Member.class)
                                .getResultList();
        System.out.println("memberList.size() = " + memberList.size());

        //삭제 - Delete
        em.remove(member);
    }
}
