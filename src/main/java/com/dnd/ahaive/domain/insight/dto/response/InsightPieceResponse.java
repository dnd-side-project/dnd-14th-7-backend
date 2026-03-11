package com.dnd.ahaive.domain.insight.dto.response;

import com.dnd.ahaive.domain.insight.entity.InsightGenerationType;
import com.dnd.ahaive.domain.insight.entity.InsightPiece;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class InsightPieceResponse {
  private List<PieceResponse> insightPieces;

  public static InsightPieceResponse from(List<InsightPiece> insightPieces) {
    List<PieceResponse> pieceResponses = insightPieces.stream()
        .map(piece -> PieceResponse.builder()
            .insightPieceId(piece.getId())
            .content(piece.getContent())
            .createdType(piece.getCreatedType())
            .createdDate(piece.getCreatedAt())
            .build())
        .toList();

    return InsightPieceResponse.builder()
        .insightPieces(pieceResponses)
        .build();
  }

  @Getter
  @Builder
  public static class PieceResponse {
    private Long insightPieceId;
    private String content;
    private InsightGenerationType createdType;
    private LocalDateTime createdDate;
  }

}
