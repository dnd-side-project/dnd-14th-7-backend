package com.dnd.ahaive.domain.insight.service;

import com.dnd.ahaive.domain.insight.dto.request.InsightCreateRequest;
import com.dnd.ahaive.domain.insight.dto.response.InsightCreateResponse;
import com.dnd.ahaive.domain.insight.entity.Insight;
import com.dnd.ahaive.domain.insight.exception.InsightAccessDeniedException;
import com.dnd.ahaive.domain.insight.repository.InsightRepository;
import com.dnd.ahaive.domain.user.entity.User;
import com.dnd.ahaive.domain.user.repository.UserRepository;
import com.dnd.ahaive.global.exception.ErrorCode;
import com.dnd.ahaive.global.security.exception.UserNotFoundException;
import com.dnd.ahaive.infra.claude.ClaudeAiClient;
import com.dnd.ahaive.infra.claude.prompt.ClaudeAiPrompt;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class InsightService {

  private final UserRepository userRepository;
  private final InsightRepository insightRepository;

  private final ClaudeAiClient claudeAiClient;

  public InsightCreateResponse createInsight(InsightCreateRequest insightCreateRequest, String uuid) {
    User user = userRepository.findByUserUuid(uuid).orElseThrow(
        () -> new UserNotFoundException(ErrorCode.USER_NOT_FOUND)
    );

    String memo = insightCreateRequest.getMemo();
    String initThought = insightCreateRequest.getMemo();
    String title = claudeAiClient.sendMessage(ClaudeAiPrompt.MEMO_TO_TITLE_PROMPT(memo));


    Insight insight = Insight.from(initThought, title, user);

    insightRepository.save(insight);

    return InsightCreateResponse.from(insight);
  }

  @Transactional(readOnly = true)
  public Insight getValidatedInsight(long insightId, String username) {
    Insight insight = insightRepository.findByIdWithUser(insightId)
            .orElseThrow(() -> new EntityNotFoundException("해당 인사이트를 찾을 수 없습니다. insightId : " + insightId));

    if (insight.isNotWrittenBy(username)) {
        throw new InsightAccessDeniedException(
                "해당 인사이트에 대한 접근 권한이 없습니다. insightId : " + insightId + ", username : " + username);
    }

    return insight;
  }
}
