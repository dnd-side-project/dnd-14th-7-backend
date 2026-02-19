package com.dnd.ahaive.domain.auth.service;

import com.dnd.ahaive.domain.auth.dto.response.TokenResponse;
import com.dnd.ahaive.domain.auth.entity.Auth;
import com.dnd.ahaive.domain.auth.exception.RefreshTokenInvalid;
import com.dnd.ahaive.domain.auth.repository.AuthRepository;
import com.dnd.ahaive.domain.user.entity.User;
import com.dnd.ahaive.domain.user.repository.UserRepository;
import com.dnd.ahaive.global.exception.ErrorCode;
import com.dnd.ahaive.global.security.exception.UserNotFoundException;
import com.dnd.ahaive.global.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

  private final UserRepository userRepository;
  private final AuthRepository authRepository;

  private final JwtTokenProvider jwtTokenProvider;

  @Transactional
  public TokenResponse refreshAccessToken(String token) {

    // 리프레시 토큰에서 사용자 정보 추출, DB에 존재하는 유저인지 확인
    String userUuid = jwtTokenProvider.getUserUuidFromToken(token);
    User user = userRepository.findByUserUuid(userUuid)
        .orElseThrow(
            () -> {
              log.error("사용자 정보가 DB에 존재하지 않습니다. userUuid: {}", userUuid);
              return new UserNotFoundException(ErrorCode.USER_NOT_FOUND);
            }
        );

    // 리프레시 토큰이 DB에 존재하는지, 일치하는지 확인
    Auth auth = authRepository.findByUserUuid(userUuid)
        .orElseThrow(
            () -> {
              log.error("리프레시 토큰이 DB에 존재하지 않습니다. userUuid: {}", userUuid);
              return new RefreshTokenInvalid(ErrorCode.REFRESH_TOKEN_INVALID);
            }
        );
    if(!auth.getRefreshToken().equals(token)){
      log.error("리프레시 토큰이 DB에 저장된 토큰과 일치하지 않습니다. userUuid: {}", userUuid);
      throw new RefreshTokenInvalid(ErrorCode.REFRESH_TOKEN_INVALID);
    }

    // auth 테이블에서 리프레시 토큰 제거
    authRepository.delete(auth);
    authRepository.flush();

    // 새로운 엑세스 토큰, 리프레시 토큰 발급
    String newAccessToken = jwtTokenProvider.createAccessToken(userUuid, user.getRole());
    String newRefreshToken = jwtTokenProvider.createRefreshToken(userUuid, user.getRole());

    // 새로운 리프레시 토큰 DB에 저장
    authRepository.save(
        Auth.of(userUuid, newRefreshToken)
    );

    // 응답 객체 생성
    return TokenResponse.of(newAccessToken, newRefreshToken);
  }



}
