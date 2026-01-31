package com.dnd.ahaive.global.security.jwt;

import jakarta.servlet.http.HttpServletRequest;
import javax.crypto.SecretKey;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

  private final SecretKey secretKey;

  @Value("${jwt.access-token-expiry}")
  private Long accessTokenExpirationTime;

  @Value("${jwt.refresh-token-expiry}")
  private Long refreshTokenExpirationTime;

  //TODO: JWT 토큰 생성, 추출, 검증 메서드 구현 필요

  public String createToken(Authentication authentication, Long expirationMillis) {
    //목데이터
    return "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c";
  }

  public void validateToken(String token) {
    return;
  }

  public String extractToken(HttpServletRequest request) {
    return null;
  }
}
