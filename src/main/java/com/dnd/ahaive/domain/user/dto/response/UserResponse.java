package com.dnd.ahaive.domain.user.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserResponse {

  private String nickname;
  private String email;

}
