package com.dnd.ahaive.domain.user.dto.request;

import com.dnd.ahaive.domain.user.entity.Position;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class RegisterPositionRequest {

  private Position position;

}
