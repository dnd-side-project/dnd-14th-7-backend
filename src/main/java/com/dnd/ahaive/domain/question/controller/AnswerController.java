package com.dnd.ahaive.domain.question.controller;

import com.dnd.ahaive.domain.question.controller.dto.AnswerRequestDto;
import com.dnd.ahaive.domain.question.service.AnswerService;
import com.dnd.ahaive.global.common.response.ResponseDTO;
import com.dnd.ahaive.global.security.core.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AnswerController {

    private final AnswerService answerService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/api/v1/questions/{id}/answer")
    public ResponseDTO<?> registerAnswer(@PathVariable("id") long questionId,
                                         @RequestBody AnswerRequestDto answerRequestDto,
                                         @AuthenticationPrincipal CustomUserDetails userDetails) {
        answerService.register(questionId, answerRequestDto, userDetails.getUuid());
        return ResponseDTO.of("success");
    }
}
