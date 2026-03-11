package com.dnd.ahaive.domain.question.controller.dto;

import com.dnd.ahaive.domain.question.entity.Answer;
import com.dnd.ahaive.domain.question.entity.Question;
import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record AnswerResponse(
         long answerId,
         long questionId,
         String questionContent,
        String answerContent,
        boolean isConverted,
        LocalDateTime createdDate
) {

    public static AnswerResponse from(Answer answer) {
        Question question = answer.getQuestion();

        return AnswerResponse.builder()
                .answerId(answer.getId())
                .questionId(question.getId())
                .questionContent(question.getContent())
                .answerContent(answer.getContent())
                .isConverted(answer.isConverted())
                .createdDate(answer.getCreatedAt())
                .build();
    }
}
