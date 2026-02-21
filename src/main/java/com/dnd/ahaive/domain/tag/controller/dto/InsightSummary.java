package com.dnd.ahaive.domain.tag.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record InsightSummary(
        @JsonProperty("insightTitle")
        String title
) {
}
