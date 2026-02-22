package com.dnd.ahaive.domain.insight.service;

import com.dnd.ahaive.domain.history.entity.AnswerInsightPromotion;
import com.dnd.ahaive.domain.history.exception.AlreadyConvertedAnswerException;
import com.dnd.ahaive.domain.history.repository.AnswerInsightPromotionRepository;
import com.dnd.ahaive.domain.insight.dto.request.AnswerToInsightRequest;
import com.dnd.ahaive.domain.insight.dto.request.InsightCreateRequest;
import com.dnd.ahaive.domain.insight.dto.request.PieceCreateRequest;
import com.dnd.ahaive.domain.insight.dto.request.PieceUpdateRequest;
import com.dnd.ahaive.domain.insight.dto.response.InsightCandidateReGenResponse;
import com.dnd.ahaive.domain.insight.dto.response.InsightCreateResponse;
import com.dnd.ahaive.domain.insight.dto.response.InsightDetailResponse;
import com.dnd.ahaive.domain.insight.dto.response.InsightListResponse;
import com.dnd.ahaive.domain.insight.dto.response.InsightPieceResponse;
import com.dnd.ahaive.domain.insight.entity.Insight;
import com.dnd.ahaive.domain.insight.entity.InsightGenerationType;
import com.dnd.ahaive.domain.insight.entity.InsightPiece;
import com.dnd.ahaive.domain.insight.entity.InsightSortType;
import com.dnd.ahaive.domain.insight.exception.InsightNotFoundException;
import com.dnd.ahaive.domain.insight.repository.InsightPieceRepository;
import com.dnd.ahaive.domain.insight.exception.InsightAccessDeniedException;
import com.dnd.ahaive.domain.insight.repository.InsightRepository;
import com.dnd.ahaive.domain.question.dto.response.AiQuestionResponse;
import com.dnd.ahaive.domain.question.entity.Answer;
import com.dnd.ahaive.domain.question.entity.Question;
import com.dnd.ahaive.domain.question.entity.QuestionStatus;
import com.dnd.ahaive.domain.question.exception.AnswerNotFoundException;
import com.dnd.ahaive.domain.question.repository.AnswerRepository;
import com.dnd.ahaive.domain.question.repository.QuestionRepository;
import com.dnd.ahaive.domain.tag.dto.response.AiTagResponse;
import com.dnd.ahaive.domain.tag.entity.InsightTag;
import com.dnd.ahaive.domain.tag.entity.TagEntity;
import com.dnd.ahaive.domain.tag.exception.TagNotFoundException;
import com.dnd.ahaive.domain.tag.repository.InsightTagRepository;
import com.dnd.ahaive.domain.tag.repository.TagEntityRepository;
import com.dnd.ahaive.domain.user.entity.User;
import com.dnd.ahaive.domain.user.repository.UserRepository;
import com.dnd.ahaive.global.exception.ErrorCode;
import com.dnd.ahaive.global.exception.InvalidInputValueException;
import com.dnd.ahaive.global.security.exception.UserNotFoundException;
import com.dnd.ahaive.infra.claude.ClaudeAiClient;
import com.dnd.ahaive.infra.claude.prompt.ClaudeAiPrompt;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import jakarta.persistence.EntityNotFoundException;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class InsightService {

  private final UserRepository userRepository;
  private final InsightRepository insightRepository;
  private final InsightPieceRepository insightPieceRepository;
  private final QuestionRepository questionRepository;
  //private final TagRepository tagRepository;
  private final TagEntityRepository tagEntityRepository;
  private final InsightTagRepository insightTagRepository;
  private final AnswerRepository answerRepository;
  private final AnswerInsightPromotionRepository answerInsightPromotionRepository;

  private final ClaudeAiClient claudeAiClient;
  private final ObjectMapper objectMapper;

  @Transactional
  public InsightCreateResponse createInsight(InsightCreateRequest insightCreateRequest, String uuid) {
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
    AiTagResponse aiTagResponse;
    AiQuestionResponse aiQuestionResponse;

    try {
      aiTagResponse = objectMapper.readValue(tagFuture.join(), AiTagResponse.class);
      aiQuestionResponse = objectMapper.readValue(questionFuture.join(), AiQuestionResponse.class);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }

    // 인사이트 저장
    Insight insight = Insight.from(initThought, title, user);
    insightRepository.save(insight);

    // 인사이트 조각 저장
    insightPieceRepository.save(InsightPiece.of(insight, insightPieceContent, InsightGenerationType.INIT));

    // 태그 저장 및 인사이트-태그 연결
    saveInsightTags(insight, aiTagResponse.getTags(), user);

    // 질문 저장
    questionRepository.saveAll(aiQuestionResponse.getQuestions().stream()
        .map(q -> Question.of(insight, q, QuestionStatus.WAITING, 1L)).toList());

    return InsightCreateResponse.from(insight);
  }

  /**
   * 사용자가 작성한 메모를(첫 생각) 기반으로 AI가 생성한 질문들을 반환합니다.
   * 질문 재생성 API 에서 사용.
   * @param initThought 첫 생각
   * @return AI가 생성한 질문들을 담은 AiQuestionResponse 객체
   */
  public AiQuestionResponse generateQuestions(String initThought) throws JsonProcessingException {
    String questionResponse = claudeAiClient.sendMessage(ClaudeAiPrompt.INIT_THOUGHT_TO_QUESTION_PROMPT(initThought));
    return objectMapper.readValue(questionResponse, AiQuestionResponse.class);
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

  @Transactional
  public InsightDetailResponse getInsightDetail(Long id, String uuid) {

    User user = userRepository.findByUserUuid(uuid).orElseThrow(
        () -> new UserNotFoundException(ErrorCode.USER_NOT_FOUND)
    );

    // 인사이트 존재 여부 및 조회 권한 검증
    Insight insight = getValidatedInsight(id, uuid);

    // 인사이트 조회수 증가
    insight.increaseView();

    // 태그 조회
    List<TagEntity> tags = insightTagRepository.findAllByInsightId(insight.getId()).stream()
        .map(InsightTag::getTagEntity)
        .toList();

    return InsightDetailResponse.of(insight, tags);
  }

  @Transactional
  public void createInsightFromAnswer(AnswerToInsightRequest answerToInsightRequest, Long insightId, String uuid) {

    User user = userRepository.findByUserUuid(uuid).orElseThrow(
        () -> new UserNotFoundException(ErrorCode.USER_NOT_FOUND)
    );

    // 인사이트 존재 여부 및 조회 권한 검증
    Insight insight = getValidatedInsight(insightId, uuid);

    // 답변이 존재하는지 확인
    Answer answer = answerRepository.findById(answerToInsightRequest.getAnswerId())
        .orElseThrow(() -> new AnswerNotFoundException(ErrorCode.ANSWER_NOT_FOUND));

    // 이미 인사이트로 변환된 이력이 있는지 확인
    if(answerInsightPromotionRepository.findByAnswerId(answerToInsightRequest.getAnswerId()).isPresent()) {
      throw new AlreadyConvertedAnswerException(ErrorCode.ALREADY_CONVERTED_ANSWER);
    }

    // 답변-인사이트 변환 및 인사이트를 저장
    String insightContent = claudeAiClient.sendMessage(ClaudeAiPrompt.ANSWER_TO_INSIGHT_PROMPT(answer.getContent()));
    InsightPiece insightPiece = InsightPiece.of(insight, insightContent,InsightGenerationType.ANSWER);

    insightPieceRepository.save(insightPiece);

    // 답변-인사이트 변환 이력 저장
    AnswerInsightPromotion answerInsightPromotion = AnswerInsightPromotion.of(insightPiece, answer);
    answerInsightPromotionRepository.save(answerInsightPromotion);

    // 답변 인사이트로 변환됨
    answer.convert();
  }

  /**
   * 태그 이름들을 받아 저장하고 인사이트와 연결합니다.
   * 같은 이름의 태그가 이미 있다면 재사용하고, 새로운 태그만 저장합니다.
   * @param insight 연결할 인사이트 객체
   * @param tagNames 태그 이름 리스트
   * @param user 태그를 저장할 사용자 객체
   */
  @Transactional
  public void saveInsightTags(Insight insight, List<String> tagNames, User user) {

    // 기존 유저 태그 조회
    Map<String, TagEntity> existingTagMap = tagEntityRepository.findAllByUserId(user.getId())
        .stream()
        .collect(Collectors.toMap(TagEntity::getTagName, tag -> tag));

    List<String> newTagNames = tagNames.stream()
        .filter(tagName -> !existingTagMap.containsKey(tagName))
        .toList();

    List<String> duplicatedTagNames = tagNames.stream()
        .filter(existingTagMap::containsKey)
        .toList();

    // 새로운 태그 저장
    List<TagEntity> newTagEntities = newTagNames.stream()
        .map(tagName -> TagEntity.of(user, tagName))
        .toList();
    tagEntityRepository.saveAll(newTagEntities);

    // 인사이트-태그 생성 (새로운 태그 + 기존 중복 태그)
    List<InsightTag> insightTags = new ArrayList<>();

    newTagEntities.stream()
        .map(tagEntity -> InsightTag.of(tagEntity, insight))
        .forEach(insightTags::add);

    duplicatedTagNames.stream()
        .map(tagName -> InsightTag.of(existingTagMap.get(tagName), insight))
        .forEach(insightTags::add);

    insightTagRepository.saveAll(insightTags);
  }

  @Transactional(readOnly = true)
  public InsightPieceResponse getInsightPieces(Long insightId, String uuid) {

    User user = userRepository.findByUserUuid(uuid).orElseThrow(
        () -> new UserNotFoundException(ErrorCode.USER_NOT_FOUND)
    );

    // 인사이트 존재 여부 및 조회 권한 검증
    Insight insight = getValidatedInsight(insightId, uuid);

    List<InsightPiece> insightPieces = insightPieceRepository.findAllByInsightIdOrderByCreatedAtAsc(insight.getId());

    return InsightPieceResponse.from(insightPieces);
  }

  @Transactional
  public void createInsightPiece(@Valid PieceCreateRequest pieceCreateRequest,
      Long insightId, String uuid) {

    User user = userRepository.findByUserUuid(uuid).orElseThrow(
        () -> new UserNotFoundException(ErrorCode.USER_NOT_FOUND)
    );

    // 인사이트 존재 여부 및 조회 권한 검증
    Insight insight = getValidatedInsight(insightId, uuid);

    InsightPiece insightPiece = InsightPiece.of(insight, pieceCreateRequest.getContent(), InsightGenerationType.SELF);

    insightPieceRepository.save(insightPiece);
  }

  @Transactional
  public void updateInsightPiece(String pieceId, PieceUpdateRequest pieceUpdateRequest, String uuid) {
    User user = userRepository.findByUserUuid(uuid).orElseThrow(
        () -> new UserNotFoundException(ErrorCode.USER_NOT_FOUND)
    );

    InsightPiece insightPiece = insightPieceRepository.findById(Long.parseLong(pieceId))
        .orElseThrow(() -> new InsightNotFoundException(ErrorCode.INSIGHT_NOT_FOUND));

    insightPiece.updateContent(pieceUpdateRequest.getContent());
  }

  @Transactional
  public void deleteInsightPiece(String pieceId, String uuid) {
    User user = userRepository.findByUserUuid(uuid).orElseThrow(
        () -> new UserNotFoundException(ErrorCode.USER_NOT_FOUND)
    );

    InsightPiece insightPiece = insightPieceRepository.findById(Long.parseLong(pieceId))
        .orElseThrow(() -> new InsightNotFoundException(ErrorCode.INSIGHT_NOT_FOUND));

    insightPieceRepository.delete(insightPiece);
  }

  @Transactional
  public InsightListResponse getInsights(int page, int size, InsightSortType sort, Long tag, String uuid) {

    User user = userRepository.findByUserUuid(uuid).orElseThrow(
        () -> new UserNotFoundException(ErrorCode.USER_NOT_FOUND)
    );


    List<Insight> insights = new ArrayList<>();
    int totalPages;
    int totalElements;

    Pageable pageable = PageRequest.of(page - 1, size,
        sort == InsightSortType.LATEST
    ? Sort.by(Sort.Direction.DESC, "createdAt")
        : Sort.by(Sort.Direction.DESC, "view"));

    if(tag == null) {

      insights = insightRepository.findAllByUserIdWithPiecesAndTags(user.getId(), pageable);
      totalElements = insightRepository.countByUserId(user.getId());
      totalPages = (int) Math.ceil((double) totalElements / size);

    } else {

      // 해당 태그가 존재하는지 확인
      tagEntityRepository.findById(tag).orElseThrow(
          () -> new TagNotFoundException(ErrorCode.TAG_NOT_FOUND)
      );

      insights = insightRepository.findAllByUserIdAndTagIdWithPiecesAndTags(user.getId(), tag, pageable);
      totalElements = insightRepository.countByUserIdAndTagId(user.getId(), tag);
      totalPages = (int) Math.ceil((double) totalElements / size);
    }


    // 조회하려는 페이지 번호가 총 페이지 수보다 큰 경우 예외 처리
    if(totalPages < page) {
      throw new InvalidInputValueException(ErrorCode.INVALID_INPUT_VALUE);
    }

    return InsightListResponse.of(insights, page, size, totalElements, totalPages);
  }

  public InsightCandidateReGenResponse reGenerateInsightCandidates(Long insightId, String uuid) {
    User user = userRepository.findByUserUuid(uuid).orElseThrow(
        () -> new UserNotFoundException(ErrorCode.USER_NOT_FOUND)
    );

    // 인사이트 존재 여부 및 조회 권한 검증
    Insight insight = getValidatedInsight(insightId, uuid);

    // 첫 생각을 기반으로 AI가 새로운 인사이트 후보 생성


  }
}
