package com.dnd.ahaive.domain.tag.repository;

import com.dnd.ahaive.domain.tag.entity.InsightTag;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface InsightTagRepository extends JpaRepository<InsightTag, Long> {

    void deleteByTagEntityIdAndInsightId(Long tagEntityId, Long insightId);

    boolean existsByInsightIdAndTagEntityId(long insightId, long tagEntityId);

    Optional<InsightTag> findByInsightIdAndTagEntityId(Long insightId, Long tagId);

    @Query("select it from InsightTag it join fetch it.tagEntity where it.insight.id = :insightId")
    List<InsightTag> findAllByInsightId(@Param("insightId") Long insightId);
}
