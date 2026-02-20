package com.dnd.ahaive.domain.question.repository;

import com.dnd.ahaive.domain.question.entity.Answer;
import com.dnd.ahaive.domain.question.entity.QuestionStatus;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AnswerRepository extends JpaRepository<Answer, Long> {

    @Query("select a from Answer a join fetch a.question q"
            + " where q.status = :questionStatus and q.insight.id = :insightId"
            + " order by a.createdAt desc")
    List<Answer> findAllByQuestionStatusAndInsightId(@Param("questionStatus") QuestionStatus status,
                                                     @Param("insightId") long insightId);

}
