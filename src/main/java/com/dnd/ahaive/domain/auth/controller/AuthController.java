package com.dnd.ahaive.domain.auth.controller;

import com.dnd.ahaive.domain.auth.dto.response.TokenResponse;
import com.dnd.ahaive.domain.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

  private final AuthService authService;

  @PostMapping("/refresh")
  public ResponseEntity<TokenResponse> refreshAccessToken {

  }

}
