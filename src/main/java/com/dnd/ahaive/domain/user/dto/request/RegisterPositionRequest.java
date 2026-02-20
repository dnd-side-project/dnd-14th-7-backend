package com.dnd.ahaive.domain.user.dto.request;

import com.dnd.ahaive.domain.user.entity.Position;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class RegisterPositionRequest {

  @NotNull(message = "포지션 값은 필수입니다.")
  private Position position;

}
