package com.dnd.ahaive.domain.user.controller;

import com.dnd.ahaive.domain.user.dto.response.UserResponse;
import com.dnd.ahaive.domain.user.service.UserService;
import com.dnd.ahaive.global.common.response.ResponseDTO;
import com.dnd.ahaive.global.security.core.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 사용자 관련 API를 담당합니다.
 * 사용자 정보 조회, 사용자 태그 전체 조회 API가 존재합니다.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

  private final UserService userService;

  @GetMapping
  public ResponseDTO<UserResponse> getUserInfo(@AuthenticationPrincipal CustomUserDetails userDetails) {
    UserResponse userResponse = userService.getUserInfo(userDetails.getUuid());
    return ResponseDTO.of(userResponse, "사용자 정보 조회에 성공하였습니다.");
  }

}
