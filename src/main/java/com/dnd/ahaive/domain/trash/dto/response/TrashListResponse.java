package com.dnd.ahaive.domain.trash.dto.response;

import com.dnd.ahaive.domain.insight.entity.Insight;
import com.dnd.ahaive.domain.insight.entity.InsightGenerationType;
import com.dnd.ahaive.domain.tag.entity.Tag;
import com.dnd.ahaive.global.exception.ErrorCode;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TrashListResponse {

  private List<ContentResponse> content;

  @Getter
  @Builder
  public static class ContentResponse {
    private Long insightId;
    private String title;
    private String confirmedContent;
    private List<TagResponse> tags;
    private LocalDateTime createdDate;
    private LocalDateTime trashedDate;
  }

  @Getter
  @Builder
  public static class TagResponse {
    private Long tagId;
    private String tagName;
  }


  public static TrashListResponse from(List<Insight> insights) {

    List<ContentResponse> content = insights.stream()
        .map(insight -> ContentResponse.builder()
            .insightId(insight.getId())
            .title(insight.getTitle())
            .confirmedContent(insight.getInsightPieces().stream()
                .filter(piece -> piece.getCreatedType().equals(InsightGenerationType.INIT))
                .findFirst()
                .orElseThrow(() -> new RuntimeException(ErrorCode.INTERNAL_SERVER_ERROR))
                .toString())
            .tags(insight.getInsightTags().stream()
                .map(tag -> TagResponse.builder()
                    .tagId(tag.getTagEntity().getId())
                    .tagName(tag.getTagEntity().getTagName())
                    .build()
                ).toList())
            .createdDate(insight.getCreatedAt())
            .trashedDate(insight.getTrashedAt())
            .build())
        .toList();

    return TrashListResponse.builder()
        .content(content)
        .build();
  }
}
