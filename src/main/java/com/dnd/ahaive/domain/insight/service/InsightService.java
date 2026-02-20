package com.dnd.ahaive.domain.insight.service;

import com.dnd.ahaive.domain.insight.dto.request.InsightCreateRequest;
import com.dnd.ahaive.domain.insight.dto.response.InsightCreateResponse;
import com.dnd.ahaive.domain.insight.entity.Insight;
import com.dnd.ahaive.domain.insight.entity.InsightGenerationType;
import com.dnd.ahaive.domain.insight.entity.InsightPiece;
import com.dnd.ahaive.domain.insight.repository.InsightPieceRepository;
import com.dnd.ahaive.domain.insight.repository.InsightRepository;
import com.dnd.ahaive.domain.user.entity.User;
import com.dnd.ahaive.domain.user.repository.UserRepository;
import com.dnd.ahaive.global.exception.ErrorCode;
import com.dnd.ahaive.global.security.exception.UserNotFoundException;
import com.dnd.ahaive.infra.claude.ClaudeAiClient;
import com.dnd.ahaive.infra.claude.prompt.ClaudeAiPrompt;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class InsightService {

  private final UserRepository userRepository;
  private final InsightRepository insightRepository;
  private final InsightPieceRepository insightPieceRepository;

  private final ClaudeAiClient claudeAiClient;

  public InsightCreateResponse createInsight(InsightCreateRequest insightCreateRequest, String uuid) {
    User user = userRepository.findByUserUuid(uuid).orElseThrow(
        () -> new UserNotFoundException(ErrorCode.USER_NOT_FOUND)
    );

    String initThought = insightCreateRequest.getMemo();

    // 제목 생성
    String title = claudeAiClient.sendMessage(ClaudeAiPrompt.INIT_THOUGHT_TO_TITLE_PROMPT(initThought));

    // 인사이트 조각 생성
    String insightPieceContent = claudeAiClient.sendMessage(ClaudeAiPrompt.INIT_THOUGHT_TO_INSIGHT_PROMPT(initThought));

    // 태그 생성

    // 질문 3개 생성

    // 인사이트 저장
    Insight insight = Insight.from(initThought, title, user);
    insightRepository.save(insight);

    // 인사이트 조각 저장
    InsightPiece insightPiece = InsightPiece.of(
        insight,
        insightPieceContent,
        InsightGenerationType.INIT
    );
    insightPieceRepository.save(insightPiece);



    return InsightCreateResponse.from(insight);
  }
}
