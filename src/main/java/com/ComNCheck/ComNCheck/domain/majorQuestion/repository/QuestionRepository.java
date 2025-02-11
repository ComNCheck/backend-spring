package com.ComNCheck.ComNCheck.domain.majorQuestion.repository;

import com.ComNCheck.ComNCheck.domain.majorQuestion.model.entity.Question;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findAllByWriterMemberId(Long writerId);
    List<Question> findByAnswerIsNotNull();
    Optional<Question> findByIdAndSharedTrue(Long id);
    List<Question> findByAnswerIsNotNullAndSharedTrue();
    List<Question> findByAnswerIsNull();
}
