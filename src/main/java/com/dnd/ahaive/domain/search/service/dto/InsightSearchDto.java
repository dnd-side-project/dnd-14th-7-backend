package com.dnd.ahaive.domain.search.service.dto;

import com.dnd.ahaive.domain.insight.entity.Insight;
import java.time.LocalDateTime;

public record InsightSearchDto(
        String title,
        String initialThought,
        String content,
        LocalDateTime createdDate
) {
    public static InsightSearchDto of(Insight insight, String content) {
        return new InsightSearchDto(
                insight.getTitle(),
                insight.getInitThought(),
                content,
                insight.getCreatedAt()
        );
    }
}
