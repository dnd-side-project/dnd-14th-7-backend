package com.dnd.ahaive.domain.tag.controller;

import com.dnd.ahaive.domain.tag.controller.dto.TagSummary;
import com.dnd.ahaive.domain.tag.controller.dto.TotalInsightSummaryByTag;
import com.dnd.ahaive.domain.tag.service.InsightTagService;
import com.dnd.ahaive.global.common.response.ResponseDTO;
import com.dnd.ahaive.global.security.core.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class InsightTagController {

    private final InsightTagService insightTagService;

    /**
     * 유저가 등록한 태그 전체 조회 API
     */
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/api/v1/users/tag")
    public ResponseDTO<?> getAllTags(@AuthenticationPrincipal CustomUserDetails userDetails) {
        TagSummary summary = insightTagService.getTagsWithInsightCount(userDetails.getUuid());
        return ResponseDTO.of(summary, "태그별 인사이트 조회");
    }

    /**
     * 태그별 인사이트 조회 API
     */
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/api/v1/insights/tag")
    public ResponseDTO<?> getInsightsGroupByTag(@AuthenticationPrincipal CustomUserDetails userDetails) {
        TotalInsightSummaryByTag summary = insightTagService.getInsightsGroupByTag(userDetails.getUuid());
        return ResponseDTO.of(summary, "태그별 인사이트 조회");
    }
}
