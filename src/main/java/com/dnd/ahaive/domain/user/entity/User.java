package com.dnd.ahaive.domain.user.entity;

import com.dnd.ahaive.global.common.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
    name = "users",
    indexes = {
        @Index(name = "idx_user_uuid", columnList = "userUuid", unique = true),
        @Index(name = "idx_provider_id", columnList = "providerId", unique = true)
    }

)
public class User extends BaseEntity {

  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String userUuid;

  @Enumerated(EnumType.STRING)
  private Role role;

  private String nickname;

  private String email;

  private int credit;

  @Enumerated(EnumType.STRING)
  private Provider provider;

  @Enumerated(EnumType.STRING)
  private Position position;

  private String providerId;

  @Builder
  private User(
      String userUuid,
      Role role,
      String nickname,
      int credit,
      String email,
      Provider provider,
      Position position,
      String providerId) {
    this.userUuid = userUuid;
    this.role = role;
    this.nickname = nickname;
    this.credit = credit;
    this.email = email;
    this.provider = provider;
    this.position = position;
    this.providerId = providerId;
  }

  public static User createGuest(int credit) {
    return User.builder()
        .userUuid(UUID.randomUUID().toString())
        .role(Role.GUEST)
        .nickname("Guest")
        .credit(credit)
        .position(Position.NONE)
        .provider(null)
        .providerId(null)
        .build();
  }

  public static User createMember(String nickname, int credit, String email, Provider provider, String providerId) {
    return User.builder()
        .userUuid(UUID.randomUUID().toString())
        .role(Role.MEMBER)
        .nickname(nickname)
        .credit(credit)
        .position(Position.NONE)
        .email(email)
        .provider(provider)
        .providerId(providerId)
        .build();
  }



}
