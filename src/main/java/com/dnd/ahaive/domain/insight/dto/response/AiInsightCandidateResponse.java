package com.dnd.ahaive.domain.insight.dto.response;

import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AiInsightCandidateResponse {
  private List<String> insightCandidates;
}
