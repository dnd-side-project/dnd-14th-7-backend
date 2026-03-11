package com.dnd.ahaive.domain.search.service.dto;

public record TagSearchDto(
        Long tagId,
        String tagName,
        Long insightCount
) {
}
