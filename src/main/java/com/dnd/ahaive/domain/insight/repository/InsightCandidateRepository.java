package com.dnd.ahaive.domain.insight.repository;

import com.dnd.ahaive.domain.insight.entity.InsightCandidate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface InsightCandidateRepository extends JpaRepository<InsightCandidate, Long> {

  @Query("select max(ic.version) from InsightCandidate ic where ic.insightPiece.id = :insightPieceId")
  Long findMaxVersionByInsightPieceId(@Param("insightPieceId") Long insightPieceId);

  @Query("select ic from InsightCandidate ic where ic.insightPiece.id = :insightPieceId order by ic.createdAt desc limit 3")
  List<InsightCandidate> findTop3ByInsightPieceIdOrderByCreatedAtDesc(@Param("insightPieceId") Long insightPieceId);

}
