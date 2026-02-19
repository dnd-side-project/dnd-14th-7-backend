package com.dnd.ahaive.domain.question.service;

import com.dnd.ahaive.domain.insight.entity.Insight;
import com.dnd.ahaive.domain.insight.exception.InsightAccessDeniedException;
import com.dnd.ahaive.domain.insight.repository.InsightRepository;
import com.dnd.ahaive.domain.question.controller.dto.AnswerResponse;
import com.dnd.ahaive.domain.question.controller.dto.TotalArchivedQuestionResponse;
import com.dnd.ahaive.domain.question.controller.dto.QuestionResponse;
import com.dnd.ahaive.domain.question.controller.dto.TotalQuestionDto;
import com.dnd.ahaive.domain.question.entity.Answer;
import com.dnd.ahaive.domain.question.entity.Question;
import com.dnd.ahaive.domain.question.entity.QuestionStatus;
import com.dnd.ahaive.domain.question.repository.AnswerRepository;
import com.dnd.ahaive.domain.question.repository.QuestionRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class QuestionService {

    private final InsightRepository insightRepository;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;

    @Transactional(readOnly = true)
    public TotalQuestionDto findQuestionAndAnswers(long insightId, String username) {
        Insight insight = insightRepository.findByIdWithUser(insightId)
                .orElseThrow(() -> new EntityNotFoundException("해당 인사이트를 찾을 수 없습니다. insightId : " + insightId));

        if (insight.isNotWrittenBy(username)) {
            throw new InsightAccessDeniedException(
                    "해당 인사이트에 대한 접근 권한이 없습니다. insightId : " + insightId + ", username : " + username);
        }

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
        Insight insight = insightRepository.findByIdWithUser(insightId)
                .orElseThrow(() -> new EntityNotFoundException("해당 인사이트를 찾을 수 없습니다. insightId : " + insightId));

        if (insight.isNotWrittenBy(username)) {
            throw new InsightAccessDeniedException(
                    "해당 인사이트에 대한 접근 권한이 없습니다. insightId : " + insightId + ", username : " + username);
        }

        List<Question> archivedQuestions = questionRepository.findAllByInsightIdAndStatusOrderByCreatedAtDesc(insightId,
                QuestionStatus.ARCHIVED);

        List<QuestionResponse> responses = archivedQuestions.stream().map(QuestionResponse::from).toList();

        return TotalArchivedQuestionResponse.from(responses);
    }

    @Transactional
    public void rollbackPreviousQuestion(long insightId, long questionId, String username) {
        Insight insight = insightRepository.findByIdWithUser(insightId)
                .orElseThrow(() -> new EntityNotFoundException("해당 인사이트를 찾을 수 없습니다. insightId : " + insightId));

        if (insight.isNotWrittenBy(username)) {
            throw new InsightAccessDeniedException(
                    "해당 인사이트에 대한 접근 권한이 없습니다. insightId : " + insightId + ", username : " + username);
        }

        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new EntityNotFoundException("해당 질문을 찾을 수 없습니다. questionsId : " + questionId));
        if (question.isNotFrom(insight)) {
            throw new IllegalArgumentException(
                    "해당 인사이트에 속한 질문이 아닙니다. insightId: " + insightId + ", questionsId: " + questionId);
        }

        question.activate();
    }
}
