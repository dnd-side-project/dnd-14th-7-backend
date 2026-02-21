package com.dnd.ahaive.domain.tag.controller;

import com.dnd.ahaive.domain.tag.controller.dto.TagRegisterRequestDto;
import com.dnd.ahaive.domain.tag.service.TagService;
import com.dnd.ahaive.global.common.response.ResponseDTO;
import com.dnd.ahaive.global.security.core.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TagController {

    private final TagService tagService;

    /**
     * 태그 생성 API
     */
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/api/v1/users/tags")
    public ResponseDTO<?> registerTag(@RequestBody TagRegisterRequestDto tagRegisterRequestDto,
                                 @AuthenticationPrincipal CustomUserDetails userDetails) {
        tagService.register(tagRegisterRequestDto, userDetails.getUuid());
        return ResponseDTO.of("success");
    }

    /**
     * 인사이트에서 태그 삭제 API
     */
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/api/v1/insights/{insightId}/tag/{tagId}")
    public ResponseDTO<?> deleteTag(@PathVariable("insightId") long insightId,
                                    @PathVariable("tagId") long tagId,
                                    @AuthenticationPrincipal CustomUserDetails userDetails) {
        tagService.delete(insightId, tagId, userDetails.getUuid());
        return ResponseDTO.of("success");
    }
}
