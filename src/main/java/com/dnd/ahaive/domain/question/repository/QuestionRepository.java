package com.dnd.ahaive.domain.question.repository;

import com.dnd.ahaive.domain.question.entity.Question;
import com.dnd.ahaive.domain.question.entity.QuestionStatus;
import com.dnd.ahaive.domain.question.service.dto.QuestionContentDto;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface QuestionRepository extends JpaRepository<Question, Long> {

    List<Question> findAllByInsightIdAndStatusOrderByCreatedAtDesc(Long insightId, QuestionStatus status);

    @Query("select q from Question q"
            + " join fetch q.insight i"
            + " join fetch i.user u"
            + " where q.id = :questionId")
    Optional<Question> findByIdWithInsightAndUser(@Param("questionId") Long questionId);

    @Query("select new com.dnd.ahaive.domain.question.service.dto.QuestionContentDto(q.id, q.content)"
            + " from Question q"
            + " where q.insight.id = :insightId and q.status = :status")
    List<QuestionContentDto> findQuestionsByInsightIdAndStatus(
            @Param("insightId") Long insightId,
            @Param("status") QuestionStatus status
    );

    @Modifying(clearAutomatically = true)
    @Query("UPDATE Question q SET q.status = :status WHERE q.id in :ids")
    int updateQuestionStatusByIdIn(@Param("status") QuestionStatus status, @Param("ids") List<Long> questionIds);

    @Query("SELECT MAX(q.version) FROM Question q WHERE q.insight.id = :insightId")
    Optional<Long> findMaxVersionByInsightId(@Param("insightId") Long insightId);
}
