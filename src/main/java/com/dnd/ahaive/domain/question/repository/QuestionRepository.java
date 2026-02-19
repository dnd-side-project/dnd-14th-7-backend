package com.dnd.ahaive.domain.question.repository;

import com.dnd.ahaive.domain.question.entity.Question;
import com.dnd.ahaive.domain.question.entity.QuestionStatus;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface QuestionRepository extends JpaRepository<Question, Long> {

    List<Question> findAllByInsightIdAndStatusOrderByCreatedAtDesc(Long insightId, QuestionStatus status);

    @Query("select q from Question q"
            + " join fetch q.insight i"
            + " join fetch i.user u"
            + " where q.id = :questionId")
    Optional<Question> findByIdWithInsightAndUser(@Param("questionId") Long questionId);
}
