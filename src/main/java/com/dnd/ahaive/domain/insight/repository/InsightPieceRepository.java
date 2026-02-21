package com.dnd.ahaive.domain.insight.repository;

import com.dnd.ahaive.domain.insight.entity.InsightPiece;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InsightPieceRepository extends JpaRepository<InsightPiece, Long> {
  List<InsightPiece> findAllByInsightIdOrderByCreatedAtAsc(Long insightId);

}
