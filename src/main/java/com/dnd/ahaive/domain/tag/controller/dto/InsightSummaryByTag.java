package com.dnd.ahaive.domain.tag.controller.dto;

import java.util.List;

public record InsightSummaryByTag(
        long tagId,
        String tagName,
        List<InsightSummary> insights,
        long insightCount
) {
}
