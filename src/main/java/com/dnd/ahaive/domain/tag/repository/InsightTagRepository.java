package com.dnd.ahaive.domain.tag.repository;

import com.dnd.ahaive.domain.tag.entity.InsightTag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InsightTagRepository extends JpaRepository<InsightTag, Long> {

    void deleteByTagEntityIdAndInsightId(Long tagEntityId, Long insightId);
}
