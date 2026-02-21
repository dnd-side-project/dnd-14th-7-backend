package com.dnd.ahaive.domain.insight.controller;

import com.dnd.ahaive.domain.insight.dto.request.AnswerToInsightRequest;
import com.dnd.ahaive.domain.insight.dto.request.InsightCreateRequest;
import com.dnd.ahaive.domain.insight.dto.response.InsightCreateResponse;
import com.dnd.ahaive.domain.insight.dto.response.InsightDetailResponse;
import com.dnd.ahaive.domain.insight.dto.response.InsightPieceResponse;
import com.dnd.ahaive.domain.insight.service.InsightService;
import com.dnd.ahaive.global.common.response.ResponseDTO;
import com.dnd.ahaive.global.security.core.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
  public ResponseEntity<ResponseDTO<InsightCreateResponse>> createInsight(@RequestBody InsightCreateRequest insightCreateRequest,
      @AuthenticationPrincipal CustomUserDetails userDetails) {

    InsightCreateResponse insightCreateResponse = insightService.createInsight(insightCreateRequest, userDetails.getUuid());

    return ResponseEntity.status(HttpStatus.CREATED)
        .body(ResponseDTO.created(insightCreateResponse, "인사이트 생성에 성공하였습니다."));
  }

  /**
   * 인사이트 상세 조회 API 입니다.
   * @param insightId 조회할 인사이트의 ID입니다.
   * @param userDetails 인증된 사용자 정보입니다.
   * @return ResponseDTO<InsightDetailResponse> 조회된 인사이트 상세 정보를 담은 응답 객체입니다.
   */
  @GetMapping("/{insightId}")
  public ResponseDTO<InsightDetailResponse> getInsightDetail(@PathVariable Long insightId,
      @AuthenticationPrincipal CustomUserDetails userDetails) {

    InsightDetailResponse insightDetailResponse = insightService.getInsightDetail(insightId, userDetails.getUuid());

    return ResponseDTO.of(insightDetailResponse, "인사이트 상세 조회에 성공하였습니다.");
  }

  /**
   * 답변을 인사이트로 만들기 API 입니다.
   */
  @PostMapping("/{insightId}/answer-blocks")
  public ResponseEntity<ResponseDTO<?>> createInsightFromAnswer(@RequestBody AnswerToInsightRequest answerToInsightRequest,
      @PathVariable Long insightId, @AuthenticationPrincipal CustomUserDetails userDetails) {

    insightService.createInsightFromAnswer(answerToInsightRequest, insightId, userDetails.getUuid());

    return ResponseEntity.status(HttpStatus.CREATED)
        .body(ResponseDTO.created(null, "답변을 인사이트로 만드는 데 성공하였습니다."));
  }

  @GetMapping("/{insightId}/list")
  public ResponseDTO<InsightPieceResponse> getInsightPieces(@PathVariable Long insightId,
      @AuthenticationPrincipal CustomUserDetails userDetails) {

    InsightPieceResponse insightPieceResponse = insightService.getInsightPieces(insightId, userDetails.getUuid());

    return ResponseDTO.of(insightPieceResponse, "인사이트 조각 조회에 성공하였습니다.");
  }

  @PostMapping("/{insightId}/pieces")



}
