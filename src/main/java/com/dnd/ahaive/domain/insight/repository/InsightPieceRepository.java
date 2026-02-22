package com.dnd.ahaive.domain.insight.repository;

import com.dnd.ahaive.domain.insight.entity.InsightGenerationType;
import com.dnd.ahaive.domain.insight.entity.InsightPiece;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface InsightPieceRepository extends JpaRepository<InsightPiece, Long> {
  List<InsightPiece> findAllByInsightIdOrderByCreatedAtAsc(Long insightId);

  @Query("select ip from InsightPiece ip where ip.insight.id = :insightId and ip.createdType = :type")
  Optional<InsightPiece> findByInsightIdAndType(Long insightId, InsightGenerationType type);



}
