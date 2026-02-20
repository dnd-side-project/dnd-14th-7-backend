package com.dnd.ahaive.domain.user.controller;

import com.dnd.ahaive.domain.user.dto.request.RegisterPositionRequest;
import com.dnd.ahaive.domain.user.dto.response.UserResponse;
import com.dnd.ahaive.domain.user.entity.Position;
import com.dnd.ahaive.domain.user.service.UserService;
import com.dnd.ahaive.global.common.response.ResponseDTO;
import com.dnd.ahaive.global.security.core.CustomUserDetails;
import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

  @PostMapping("/position")
  public ResponseDTO<?> registerPosition(@RequestBody @Valid RegisterPositionRequest registerPositionRequest,
      @AuthenticationPrincipal CustomUserDetails userDetails) {

    userService.registerPosition(userDetails.getUuid(), registerPositionRequest);
    return ResponseDTO.of("사용자 포지션 등록에 성공하였습니다.");
  }

}
