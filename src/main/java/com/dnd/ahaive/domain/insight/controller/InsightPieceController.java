package com.dnd.ahaive.domain.insight.controller;

import com.dnd.ahaive.domain.insight.dto.request.PieceUpdateRequest;
import com.dnd.ahaive.domain.insight.dto.response.InsightListResponse;
import com.dnd.ahaive.domain.insight.entity.InsightSortType;
import com.dnd.ahaive.domain.insight.service.InsightService;
import com.dnd.ahaive.global.common.response.ResponseDTO;
import com.dnd.ahaive.global.security.core.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/insight-pieces")
public class InsightPieceController {

  private final InsightService insightService;

  @PatchMapping("/{pieceId}")
  public ResponseDTO<?> updateInsightPiece(@RequestBody PieceUpdateRequest pieceUpdateRequest,
      @PathVariable("pieceId") String pieceId, @AuthenticationPrincipal CustomUserDetails customUserDetails) {

    insightService.updateInsightPiece(pieceId, pieceUpdateRequest, customUserDetails.getUuid());

    return ResponseDTO.of("인사이트 조각 수정에 성공하였습니다.");
  }

  @DeleteMapping("/{pieceId}")
  public ResponseDTO<?> deleteInsightPiece(@PathVariable("pieceId") String pieceId,
      @AuthenticationPrincipal CustomUserDetails customUserDetails) {

    insightService.deleteInsightPiece(pieceId, customUserDetails.getUuid());

    return ResponseDTO.of("인사이트 조각 삭제에 성공하였습니다.");
  }


  @GetMapping
  public ResponseDTO<InsightListResponse> getInsights(
      @RequestParam(defaultValue = "1") int page,
      @RequestParam(defaultValue = "4") int size,
      @RequestParam(defaultValue = "LATEST") InsightSortType sort,
      @RequestParam(required = false) Long tag,
      @AuthenticationPrincipal CustomUserDetails customUserDetails
  ) {
    InsightListResponse insightListResponse = insightService.getInsights(page, size, sort, tag, customUserDetails.getUuid());

    return ResponseDTO.of(insightListResponse, "인사이트 조회에 성공하였습니다.");
  }



}
