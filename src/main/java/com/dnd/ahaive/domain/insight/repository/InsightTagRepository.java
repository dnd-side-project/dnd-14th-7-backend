package com.dnd.ahaive.domain.insight.repository;

import com.dnd.ahaive.domain.insight.entity.InsightTag;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface InsightTagRepository extends JpaRepository<InsightTag, Long> {

  @Query("select it from InsightTag it join fetch it.tagEntity where it.insight.id = :insightId")
  List<InsightTag> findAllByInsightId(@Param("insightId") Long insightId);

}
