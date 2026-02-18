package com.dnd.ahaive.domain.user.dto.response;

import com.dnd.ahaive.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserResponse {

  private String nickname;
  private String email;
  private int credit;

  public static UserResponse from(User user) {
    return UserResponse.builder()
        .nickname(user.getNickname())
        .email(user.getEmail())
        .credit(user.getCredit())
        .build();
  }

}
