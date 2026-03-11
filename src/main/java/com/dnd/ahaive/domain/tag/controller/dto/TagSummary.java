package com.dnd.ahaive.domain.tag.controller.dto;

import com.dnd.ahaive.domain.tag.service.dto.TagInsightCount;
import java.util.List;

public record TagSummary(
        List<TagInsightCount> tags
) {
}
