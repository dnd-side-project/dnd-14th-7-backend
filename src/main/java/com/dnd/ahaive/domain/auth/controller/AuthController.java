package com.dnd.ahaive.domain.auth.controller;

import com.dnd.ahaive.domain.auth.dto.response.TokenResponse;
import com.dnd.ahaive.domain.auth.exception.RefreshTokenInvalid;
import com.dnd.ahaive.domain.auth.exception.TokenInvalid;
import com.dnd.ahaive.domain.auth.exception.TokenInvalidType;
import com.dnd.ahaive.domain.auth.service.AuthService;
import com.dnd.ahaive.global.common.response.ResponseDTO;
import com.dnd.ahaive.global.exception.ErrorCode;
import com.dnd.ahaive.global.security.jwt.JwtTokenProvider;
import com.dnd.ahaive.global.security.jwt.JwtTokenType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

  private final AuthService authService;
  private final JwtTokenProvider jwtTokenProvider;

  @PostMapping("/refresh")
  public ResponseEntity<ResponseDTO<TokenResponse>> refreshAccessToken(
      @RequestHeader(value = "Authorization", required = false) String refreshToken) {

    // 리프레시 토큰이 헤더에 없을 경우
    if(refreshToken == null || refreshToken.isEmpty()){
      throw new TokenInvalid(ErrorCode.TOKEN_INVALID);
    }

    // 리프레시 토큰 형식이 잘못됐을 경우(Bearer로 시작하지 않을 경우)
    if(!refreshToken.startsWith("Bearer ")){
      throw new TokenInvalidType(ErrorCode.TOKEN_INVALID_TYPE);
    }

    String token = refreshToken.substring(7);

    // 토큰 타입 추출 실패하거나(TokenExtractionException)
    JwtTokenType tokenType = jwtTokenProvider.getTypeFromToken(token);

    //타입이 리프레시 토큰이 아닐 경우
    if(tokenType != JwtTokenType.REFRESH){
      throw new TokenInvalidType(ErrorCode.TOKEN_INVALID_TYPE);
    }

    // 리프레시 토큰 검증 실패할 경우
    if(!jwtTokenProvider.isValidToken(token)){
      throw new RefreshTokenInvalid(ErrorCode.REFRESH_TOKEN_INVALID);
    }

    TokenResponse tokenResponse = authService.refreshAccessToken(token);
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(
            ResponseDTO.of(tokenResponse, "엑세스, 리프레시 토큰이 재발급되었습니다.")
        );

  }

}
