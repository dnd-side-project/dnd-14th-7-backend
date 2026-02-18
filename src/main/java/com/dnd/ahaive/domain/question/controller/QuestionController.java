package com.dnd.ahaive.domain.question.controller;

import com.dnd.ahaive.domain.question.controller.dto.TotalQuestionDto;
import com.dnd.ahaive.domain.question.service.QuestionService;
import com.dnd.ahaive.global.common.response.ResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class QuestionController {

    private final QuestionService questionService;

    @GetMapping("/api/v1/insights/{id}/questions")
    @ResponseStatus(code = HttpStatus.OK)
    public ResponseDTO<TotalQuestionDto> questions(@PathVariable("id") long insightId, @AuthenticationPrincipal
                                                   UserDetails userDetails) {
        TotalQuestionDto totalQuestionDto = questionService.findQuestionAndAnswers(insightId, userDetails.getUsername());
        return ResponseDTO.of(totalQuestionDto, "success");
    }

}
