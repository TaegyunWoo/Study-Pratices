package practice.testCode.study;


import org.springframework.data.jpa.repository.JpaRepository;
import practice.testCode.domain.Study;

public interface StudyRepository extends JpaRepository<Study, Long> {

}
