package com.ComNCheck.ComNCheck.domain.developerQuestion.repository;

import com.ComNCheck.ComNCheck.domain.developerQuestion.model.entity.DeveloperQuestion;
import com.ComNCheck.ComNCheck.domain.member.model.entity.Member;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;



public interface DeveloperQuestionRepository extends JpaRepository<DeveloperQuestion, Long> {

    //Optional<DeveloperQuestion> findByDeveloperQuestionId(Long id);
    List<DeveloperQuestion> findAllByWriter(Member writer);

}
