package com.dnd.ahaive.domain.question.controller.dto;

import java.util.List;
import lombok.Builder;

@Builder
public record TotalQuestionDto (
        List<QuestionResponse> questions,
        List<AnswerResponse> answerCards
) {

    public static TotalQuestionDto of(List<QuestionResponse> questions, List<AnswerResponse> answerCards) {
        return TotalQuestionDto.builder()
                .questions(questions)
                .answerCards(answerCards)
                .build();
    }
}
