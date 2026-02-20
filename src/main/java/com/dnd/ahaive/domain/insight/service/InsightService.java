package com.dnd.ahaive.domain.insight.service;

import com.dnd.ahaive.domain.insight.dto.request.InsightCreateRequest;
import com.dnd.ahaive.domain.insight.dto.response.InsightCreateResponse;
import com.dnd.ahaive.domain.insight.entity.Insight;
import com.dnd.ahaive.domain.insight.entity.InsightGenerationType;
import com.dnd.ahaive.domain.insight.entity.InsightPiece;
import com.dnd.ahaive.domain.insight.repository.InsightPieceRepository;
import com.dnd.ahaive.domain.insight.repository.InsightRepository;
import com.dnd.ahaive.domain.question.dto.response.AiQuestionResponse;
import com.dnd.ahaive.domain.question.entity.Question;
import com.dnd.ahaive.domain.question.entity.QuestionStatus;
import com.dnd.ahaive.domain.question.repository.QuestionRepository;
import com.dnd.ahaive.domain.tag.dto.response.AiTagResponse;
import com.dnd.ahaive.domain.tag.entity.Tag;
import com.dnd.ahaive.domain.tag.repository.TagRepository;
import com.dnd.ahaive.domain.user.entity.User;
import com.dnd.ahaive.domain.user.repository.UserRepository;
import com.dnd.ahaive.global.exception.ErrorCode;
import com.dnd.ahaive.global.security.exception.UserNotFoundException;
import com.dnd.ahaive.infra.claude.ClaudeAiClient;
import com.dnd.ahaive.infra.claude.prompt.ClaudeAiPrompt;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

@Slf4j
@Service
@RequiredArgsConstructor
public class InsightService {

  private final UserRepository userRepository;
  private final InsightRepository insightRepository;
  private final InsightPieceRepository insightPieceRepository;
  private final QuestionRepository questionRepository;

  private final ClaudeAiClient claudeAiClient;
  private final ObjectMapper objectMapper;
  private final TagRepository tagRepository;

  @Transactional
  public InsightCreateResponse createInsightParallel(InsightCreateRequest insightCreateRequest, String uuid) {
    User user = userRepository.findByUserUuid(uuid).orElseThrow(
        () -> new UserNotFoundException(ErrorCode.USER_NOT_FOUND)
    );

    String initThought = insightCreateRequest.getMemo();

    // AI 호출 병렬 처리
    CompletableFuture<String> titleFuture = CompletableFuture.supplyAsync(() ->
        claudeAiClient.sendMessage(ClaudeAiPrompt.INIT_THOUGHT_TO_TITLE_PROMPT(initThought)));

    CompletableFuture<String> insightPieceFuture = CompletableFuture.supplyAsync(() ->
        claudeAiClient.sendMessage(ClaudeAiPrompt.INIT_THOUGHT_TO_INSIGHT_PROMPT(initThought)));

    CompletableFuture<String> tagFuture = CompletableFuture.supplyAsync(() ->
        claudeAiClient.sendMessage(ClaudeAiPrompt.INIT_THOUGHT_TO_TAG_PROMPT(initThought)));

    CompletableFuture<String> questionFuture = CompletableFuture.supplyAsync(() ->
        claudeAiClient.sendMessage(ClaudeAiPrompt.INIT_THOUGHT_TO_QUESTION_PROMPT(initThought)));

    CompletableFuture.allOf(titleFuture, insightPieceFuture, tagFuture, questionFuture).join();

    String title = titleFuture.join();
    String insightPieceContent = insightPieceFuture.join();
    AiTagResponse aiTagResponse = objectMapper.readValue(tagFuture.join(), AiTagResponse.class);
    AiQuestionResponse aiQuestionResponse = objectMapper.readValue(questionFuture.join(), AiQuestionResponse.class);

    // 인사이트 저장
    Insight insight = Insight.from(initThought, title, user);
    insightRepository.save(insight);

    // 인사이트 조각 저장
    insightPieceRepository.save(InsightPiece.of(insight, insightPieceContent, InsightGenerationType.INIT));

    // 태그 저장
    tagRepository.saveAll(aiTagResponse.getTags().stream()
        .map(tagName -> Tag.of(user, insight, tagName)).toList());

    // 질문 저장
    questionRepository.saveAll(aiQuestionResponse.getQuestions().stream()
        .map(q -> Question.of(insight, q, QuestionStatus.WAITING, 1L)).toList());

    return InsightCreateResponse.from(insight);
  }

  public AiQuestionResponse generateQuestions(String initThought) {
    String questionResponse = claudeAiClient.sendMessage(ClaudeAiPrompt.INIT_THOUGHT_TO_QUESTION_PROMPT(initThought));
    return objectMapper.readValue(questionResponse, AiQuestionResponse.class);
  }

}
