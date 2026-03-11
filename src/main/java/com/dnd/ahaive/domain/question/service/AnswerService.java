package com.dnd.ahaive.domain.question.service;

import com.dnd.ahaive.domain.insight.entity.Insight;
import com.dnd.ahaive.domain.insight.exception.InsightAccessDeniedException;
import com.dnd.ahaive.domain.question.controller.dto.AnswerRequestDto;
import com.dnd.ahaive.domain.question.entity.Answer;
import com.dnd.ahaive.domain.question.entity.Question;
import com.dnd.ahaive.domain.question.repository.AnswerRepository;
import com.dnd.ahaive.domain.question.repository.QuestionRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AnswerService {

    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;

    @Transactional
    public Long register(long questionId, AnswerRequestDto answerRequestDto, String username) {
        // 질문 조회
        Question question = questionRepository.findByIdWithInsightAndUser(questionId)
                .orElseThrow(() -> new EntityNotFoundException("해당 질문을 찾을 수 없습니다. questionId : " + questionId));

        // 소유자 검증
        Insight insight = question.getInsight();
        if (insight.isNotWrittenBy(username)) {
            throw new InsightAccessDeniedException(
                    "해당 인사이트에 대한 접근 권한이 없습니다. insightId : " + insight.getId() + ", username : " + username);
        }

        // 답변 등록
        if (question.isCompleted()) {
            throw new IllegalStateException("이미 답변이 완료된 질문입니다. questionId: " + question.getId());
        }

        Answer answer = Answer.of(question, answerRequestDto.content(), false);
        Answer savedAnswer = answerRepository.save(answer);

        question.complete();

        return savedAnswer.getId();
    }


}
