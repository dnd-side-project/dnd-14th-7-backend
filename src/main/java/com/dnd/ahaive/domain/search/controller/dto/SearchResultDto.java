package com.dnd.ahaive.domain.search.controller.dto;

import com.dnd.ahaive.domain.search.service.dto.InsightSearchDto;
import com.dnd.ahaive.domain.search.service.dto.TagSearchDto;
import java.util.List;

public record SearchResultDto(
        List<TagSearchDto> tags,
        List<InsightSearchDto> insights
) {
}
