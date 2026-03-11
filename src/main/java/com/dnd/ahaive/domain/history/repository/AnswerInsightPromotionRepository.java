package com.dnd.ahaive.domain.history.repository;

import com.dnd.ahaive.domain.history.entity.AnswerInsightPromotion;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnswerInsightPromotionRepository extends
    JpaRepository<AnswerInsightPromotion, Long> {

  Optional<AnswerInsightPromotion> findByAnswerId(Long answerId);



}
