package com.dnd.ahaive.domain.question.controller;

import com.dnd.ahaive.domain.question.controller.dto.AnswerRequestDto;
import com.dnd.ahaive.domain.question.service.AnswerService;
import com.dnd.ahaive.global.common.response.ResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AnswerController {

    private final AnswerService answerService;

    @GetMapping("/api/v1/questions/{id}/answer")
    public ResponseDTO<?> registerAnswer(@PathVariable("id") long questionId,
                                         @RequestBody AnswerRequestDto answerRequestDto,
                                         @AuthenticationPrincipal UserDetails userDetails) {
        answerService.register(questionId, answerRequestDto, userDetails.getUsername());
        return ResponseDTO.of("success");
    }
}
