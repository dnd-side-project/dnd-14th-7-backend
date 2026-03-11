package com.dnd.ahaive.domain.tag.service.dto;

public record TagInsightCount(
        long tagId,
        String tagName,
        long insightCount
) {
}
