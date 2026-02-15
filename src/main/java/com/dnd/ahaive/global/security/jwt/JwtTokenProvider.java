package com.dnd.ahaive.global.security.jwt;

import com.dnd.ahaive.domain.user.entity.Role;
import com.dnd.ahaive.global.exception.ErrorCode;
import com.dnd.ahaive.global.security.exception.InvalidAccessJwtException;
import com.dnd.ahaive.global.security.exception.InvalidRefrashJwtException;
import com.dnd.ahaive.global.security.exception.TokenExtractionException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;
import javax.crypto.SecretKey;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

  private final SecretKey secretKey;

  @Value("${jwt.access-token-expiry}")
  private Long accessTokenExpirationTime;

  @Value("${jwt.refresh-token-expiry}")
  private Long refreshTokenExpirationTime;

  private String createToken(String userUuid, Long expirationMillis, JwtTokenType type, Role role) {

    Date expiryDate = new Date(new Date().getTime() + expirationMillis);

    return Jwts.builder()
        .claim("userUuid", userUuid)
        .claim("role", role.name())
        .claim("type", type.name())
        .issuedAt(new Date())
        .expiration(expiryDate)
        .signWith(secretKey)
        .compact();
  }

  public String createAccessToken(String userUuid, Role role) {
    return this.createToken(
        userUuid
        , accessTokenExpirationTime
        , JwtTokenType.ACCESS
        , role
    );
  }

  public String createRefreshToken(String userUuid, Role role) {
    return this.createToken(
        userUuid
        , refreshTokenExpirationTime
        , JwtTokenType.REFRESH
        , role
    );
  }



  public void validateToken(String token) {
    try {
      Jwts.parser()
          .verifyWith(secretKey)
          .build()
          .parseSignedClaims(token);
    } catch (ExpiredJwtException e) {
      log.error("토큰이 만료되었습니다: {}", e.getMessage());
      if(getTypeFromToken(token) == JwtTokenType.ACCESS) {
        throw new InvalidAccessJwtException(ErrorCode.ACCESS_TOKEN_INVALID);
      } else {
        throw new InvalidRefrashJwtException(ErrorCode.REFRESH_TOKEN_INVALID);
      }
    } catch (Exception e) {
      log.error("유효하지 않은 토큰입니다: {}", e.getMessage());
      if(getTypeFromToken(token) == JwtTokenType.ACCESS) {
        throw new InvalidAccessJwtException(ErrorCode.ACCESS_TOKEN_INVALID);
      } else {
        throw new InvalidRefrashJwtException(ErrorCode.REFRESH_TOKEN_INVALID);
      }
    }
  }

  public boolean isValidToken(String token) {
    try {
      Jwts.parser()
          .verifyWith(secretKey)
          .build()
          .parseSignedClaims(token);
      return true;
    } catch (Exception e) {
      return false;
    }
  }



  public JwtTokenType getTypeFromToken(String token) {
    try {
      String[] chunks = token.split("\\.");
      Base64.Decoder decoder = Base64.getUrlDecoder();

      String payload = new String(decoder.decode(chunks[1]));
      ObjectMapper mapper = new ObjectMapper();
      JsonNode payloadJson = mapper.readTree(payload);

      String type = payloadJson.get("type").asText();
      return JwtTokenType.valueOf(type);

    } catch (Exception e) {
      log.error("토큰 타입 추출 실패: {}", e.getMessage());
      throw new TokenExtractionException(ErrorCode.TOKEN_INVALID);
    }
  }

  public Role getRoleFromToken(String token) {
    String role = Jwts.parser()
        .verifyWith(secretKey)
        .build()
        .parseSignedClaims(token)
        .getPayload()
        .get("role", String.class);

    return Role.valueOf(role);
  }

  public String getUserUuidFromToken(String token) {
    return Jwts.parser()
        .verifyWith(secretKey)
        .build()
        .parseSignedClaims(token)
        .getPayload()
        .get("userUuid", String.class);
  }

  public String getTokenFromRequest(HttpServletRequest request) {
    String token = request.getHeader("Authorization");

    if (token != null && token.startsWith("Bearer ")) {
      token = token.substring(7);
    } else {
      log.error("Authorization 헤더가 없거나 Bearer 로 시작하지 않습니다.");
      return null;
    }

    return token;

  }

  public String extractToken(HttpServletRequest request) {
    String authHeader = request.getHeader("Authorization");

    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      log.error("Authorization 헤더가 없거나 Bearer 로 시작하지 않습니다.");
      throw new TokenExtractionException(ErrorCode.TOKEN_EXTRACTION_FAILED);
    }

    return authHeader.substring(7);
  }
}
