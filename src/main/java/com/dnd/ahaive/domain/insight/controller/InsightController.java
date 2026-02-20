package com.dnd.ahaive.domain.insight.controller;

import com.dnd.ahaive.domain.insight.dto.request.InsightCreateRequest;
import com.dnd.ahaive.domain.insight.dto.response.InsightCreateResponse;
import com.dnd.ahaive.domain.insight.service.InsightService;
import com.dnd.ahaive.global.common.response.ResponseDTO;
import com.dnd.ahaive.global.security.core.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/insights")
public class InsightController {

  private final InsightService insightService;

  @PostMapping
  public ResponseDTO<InsightCreateResponse> createInsight(@RequestBody InsightCreateRequest insightCreateRequest,
      @AuthenticationPrincipal CustomUserDetails userDetails) {

    InsightCreateResponse insightCreateResponse = insightService.createInsight(insightCreateRequest, userDetails.getUuid());

    return ResponseDTO.of(insightCreateResponse, "인사이트 생성에 성공하였습니다.");
  }

}
