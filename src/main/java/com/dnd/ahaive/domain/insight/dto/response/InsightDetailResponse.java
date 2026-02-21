package com.dnd.ahaive.domain.insight.dto.response;

import com.dnd.ahaive.domain.insight.entity.Insight;
import com.dnd.ahaive.domain.tag.entity.Tag;
import com.dnd.ahaive.domain.tag.entity.TagEntity;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class InsightDetailResponse {

  private Long insightId;
  private String initialThought;
  private String title;

  private List<TagResponse> tags;

  private LocalDateTime createdDate;
  private LocalDateTime updatedDate;

  public static InsightDetailResponse of(Insight insight, List<TagEntity> tagEntities) {
    return InsightDetailResponse.builder()
        .insightId(insight.getId())
        .initialThought(insight.getInitThought())
        .title(insight.getTitle())
        .tags(tagEntities.stream()
            .map(tag -> TagResponse.builder()
                .tagId(tag.getId())
                .tagName(tag.getTagName())
                .build())
            .toList())
        .createdDate(insight.getCreatedAt())
        .updatedDate(insight.getUpdatedAt())
        .build();
  }


  @Getter
  @Builder
  public static class TagResponse {
    private Long tagId;
    private String tagName;
  }

}
