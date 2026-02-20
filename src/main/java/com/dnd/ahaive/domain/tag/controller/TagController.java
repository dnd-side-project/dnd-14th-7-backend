package com.dnd.ahaive.domain.tag.controller;

import com.dnd.ahaive.domain.tag.controller.dto.TagRegisterRequestDto;
import com.dnd.ahaive.domain.tag.service.TagService;
import com.dnd.ahaive.global.common.response.ResponseDTO;
import com.dnd.ahaive.global.security.core.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TagController {

    private final TagService tagService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/api/v1/users/tags")
    public ResponseDTO<?> registerTag(@RequestBody TagRegisterRequestDto tagRegisterRequestDto,
                                 @AuthenticationPrincipal CustomUserDetails userDetails) {
        tagService.register(tagRegisterRequestDto, userDetails.getUuid());
        return ResponseDTO.of("success");
    }
}
