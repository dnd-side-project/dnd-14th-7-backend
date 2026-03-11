package com.dnd.ahaive.domain.insight.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PieceCreateRequest {
  @NotNull(message = "content는 필수 입력값입니다.")
  private String content;
}
