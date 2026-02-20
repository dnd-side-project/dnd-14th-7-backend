package com.dnd.ahaive.domain.question.controller.dto;

import java.util.List;

public record TotalArchivedQuestionResponse(
        List<QuestionResponse> archivedQuestions
) {
    public static TotalArchivedQuestionResponse from(List<QuestionResponse> responses) {
        return new TotalArchivedQuestionResponse(responses);
    }
}
