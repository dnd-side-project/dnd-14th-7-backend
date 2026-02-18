package com.dnd.ahaive.domain.question.repository;

import com.dnd.ahaive.domain.question.entity.Question;
import com.dnd.ahaive.domain.question.entity.QuestionStatus;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question, Long> {

    List<Question> findAllByInsightIdAndStatusOrderByCreatedAtDesc(Long insightId, QuestionStatus status);
}
