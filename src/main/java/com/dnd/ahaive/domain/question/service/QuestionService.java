package com.dnd.ahaive.domain.question.service;

import com.dnd.ahaive.domain.insight.entity.Insight;
import com.dnd.ahaive.domain.insight.repository.InsightRepository;
import com.dnd.ahaive.domain.insight.service.InsightService;
import com.dnd.ahaive.domain.question.controller.dto.AnswerResponse;
import com.dnd.ahaive.domain.question.controller.dto.TotalArchivedQuestionResponse;
import com.dnd.ahaive.domain.question.controller.dto.QuestionResponse;
import com.dnd.ahaive.domain.question.controller.dto.TotalQuestionDto;
import com.dnd.ahaive.domain.question.entity.Answer;
import com.dnd.ahaive.domain.question.entity.Question;
import com.dnd.ahaive.domain.question.entity.QuestionStatus;
import com.dnd.ahaive.domain.question.repository.AnswerRepository;
import com.dnd.ahaive.domain.question.repository.QuestionRepository;
import com.dnd.ahaive.domain.question.service.dto.QuestionContentDto;
import com.dnd.ahaive.domain.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class QuestionService {

    private final InsightService insightService;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final InsightRepository insightRepository;

    @Transactional(readOnly = true)
    public TotalQuestionDto findQuestionAndAnswers(long insightId, String username) {
        insightService.getValidatedInsight(insightId, username);

        List<Question> waitingQuestions = questionRepository.findAllByInsightIdAndStatusOrderByCreatedAtDesc(insightId,
                QuestionStatus.WAITING);

        List<Answer> answers = answerRepository.findAllByQuestionStatusAndInsightId(
                QuestionStatus.COMPLETED, insightId);

        List<QuestionResponse> questionResponses = waitingQuestions.stream().map(QuestionResponse::from).toList();

        List<AnswerResponse> answerResponses = answers.stream().map(AnswerResponse::from).toList();

        return TotalQuestionDto.of(questionResponses, answerResponses);
    }

    @Transactional(readOnly = true)
    public TotalArchivedQuestionResponse findPreviousQuestion(long insightId, String username) {
        insightService.getValidatedInsight(insightId, username);

        List<Question> archivedQuestions = questionRepository.findAllByInsightIdAndStatusOrderByCreatedAtDesc(insightId,
                QuestionStatus.ARCHIVED);

        List<QuestionResponse> responses = archivedQuestions.stream().map(QuestionResponse::from).toList();

        return TotalArchivedQuestionResponse.from(responses);
    }

    @Transactional
    public void rollbackPreviousQuestion(long insightId, long questionId, String username) {
        Insight insight = insightService.getValidatedInsight(insightId, username);

        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new EntityNotFoundException("해당 질문을 찾을 수 없습니다. questionsId : " + questionId));
        if (question.isNotFrom(insight)) {
            throw new IllegalArgumentException(
                    "해당 인사이트에 속한 질문이 아닙니다. insightId: " + insightId + ", questionsId: " + questionId);
        }

        question.activate();
    }

    @Transactional(readOnly = true)
    public List<QuestionContentDto> getCurrentQuestions(Long insightId) {
        return questionRepository.findQuestionsByInsightIdAndStatus(insightId, QuestionStatus.WAITING);
    }

    @Transactional
    public void storeCurrentQuestionsAndSaveNewQuestions(Insight insight,
                                                         List<QuestionContentDto> currentQuestionContents,
                                                         List<String> collectedQuestionContents) {
        Insight managedInsight = insightRepository.findById(insight.getId())
                .orElseThrow(() -> new EntityNotFoundException("인사이트가 존재하지 않습니다. insightId: " + insight.getId()));

        // 현재 인사이트 보관
        List<Long> currentQuestionIds = currentQuestionContents.stream().map(QuestionContentDto::questionId).toList();
        questionRepository.updateQuestionStatusByIdIn(QuestionStatus.ARCHIVED, currentQuestionIds);

        // 다음 버전 계산
        int nextVersion = questionRepository.findMaxVersionByInsightId(insight.getId()).orElse(0) + 1;

        // 새로운 인사이트 저장
        List<Question> newQuestions = collectedQuestionContents.stream()
                .map(content ->
                        Question.of(
                                managedInsight,
                                content,
                                QuestionStatus.WAITING,
                        nextVersion
                        )
                )
                .collect(Collectors.toList());

        questionRepository.saveAll(newQuestions);
    }
}
