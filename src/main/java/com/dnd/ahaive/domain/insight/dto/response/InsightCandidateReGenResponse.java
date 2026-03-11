package com.dnd.ahaive.domain.insight.dto.response;
import com.dnd.ahaive.domain.insight.entity.Insight;
import com.dnd.ahaive.domain.insight.entity.InsightCandidate;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class InsightCandidateReGenResponse {

  private Long insightId;
  private Long newVersion;
  private List<CandidateResponse> candidates;

  @Getter
  @Builder
  public static class CandidateResponse {
    private Long candidateId;
    private String content;
    private Long version;
  }

  public static InsightCandidateReGenResponse of(Insight insight, List<InsightCandidate> candidates) {
    return InsightCandidateReGenResponse.builder()
        .insightId(insight.getId())
        .newVersion(candidates.get(0).getVersion())
        .candidates(
            candidates.stream()
                .map(candidate -> CandidateResponse.builder()
                    .candidateId(candidate.getId())
                    .content(candidate.getContent())
                    .version(candidate.getVersion()).build())
                .toList()
        ).build();
  }

}
