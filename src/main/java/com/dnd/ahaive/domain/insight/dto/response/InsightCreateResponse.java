package com.dnd.ahaive.domain.insight.dto.response;

import com.dnd.ahaive.domain.insight.entity.Insight;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class InsightCreateResponse {

  private Long insightId;

  public static InsightCreateResponse from(Insight insight){
    return InsightCreateResponse.builder()
        .insightId(insight.getId())
        .build();
  }

}
