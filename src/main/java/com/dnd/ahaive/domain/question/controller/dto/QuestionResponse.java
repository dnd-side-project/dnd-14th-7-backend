package com.dnd.ahaive.domain.question.controller.dto;

import com.dnd.ahaive.domain.question.entity.Question;
import com.dnd.ahaive.domain.question.entity.QuestionStatus;
import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record QuestionResponse (
        long questionId,
        String content,
        QuestionStatus status,
        LocalDateTime createdDate
) {

    public static QuestionResponse from(Question question) {
        return QuestionResponse.builder()
                .questionId(question.getId())
                .content(question.getContent())
                .status(question.getStatus())
                .createdDate(question.getCreatedAt())
                .build();
    }
}
