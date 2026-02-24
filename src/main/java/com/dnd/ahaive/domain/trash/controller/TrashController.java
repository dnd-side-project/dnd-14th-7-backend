package com.dnd.ahaive.domain.trash.controller;

import com.dnd.ahaive.domain.trash.dto.response.TrashListResponse;
import com.dnd.ahaive.domain.trash.service.TrashService;
import com.dnd.ahaive.global.common.response.ResponseDTO;
import com.dnd.ahaive.global.security.core.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/trash")
public class TrashController {

  private final TrashService trashService;


  @GetMapping
  public ResponseDTO<TrashListResponse> getTrashInsights(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
    TrashListResponse trashListResponse = trashService.getTrashInsights(customUserDetails.getUuid());

    return ResponseDTO.of(trashListResponse, "휴지통 전체 조회에 성공하였습니다.");
  }
}
