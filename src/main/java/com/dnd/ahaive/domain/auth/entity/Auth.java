package com.dnd.ahaive.domain.auth.entity;

import com.dnd.ahaive.global.common.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
    name = "auth",
    indexes = @Index(name = "idx_user_uuid", columnList = "userUuid", unique = true)
)
public class Auth extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String userUuid;

  private String refreshToken;

  @Builder
  private Auth(String userUuid, String refreshToken) {
    this.userUuid = userUuid;
    this.refreshToken = refreshToken;
  }

  public void updateRefreshToken(String refreshToken) {
    this.refreshToken = refreshToken;
  }

  public static Auth of(String userUuid, String refreshToken) {
    return Auth.builder()
        .userUuid(userUuid)
        .refreshToken(refreshToken)
        .build();
  }



}
