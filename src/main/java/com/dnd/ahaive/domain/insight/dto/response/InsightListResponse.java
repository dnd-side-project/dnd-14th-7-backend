package com.dnd.ahaive.domain.insight.dto.response;

import com.dnd.ahaive.domain.insight.entity.Insight;
import com.dnd.ahaive.domain.insight.entity.InsightGenerationType;
import com.dnd.ahaive.domain.insight.entity.InsightPiece;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class InsightListResponse {

  private List<ContentResponse> content;
  private PageResponse page;

  @Getter
  @Builder
  public static class ContentResponse {
    private Long insightId;
    private String title;
    private String confirmedContent;
    private List<TagResponse> tags;
    private LocalDateTime createdDate;
  }

  @Getter
  @Builder
  public static class PageResponse {
    private int number;
    private int size;
    private int totalElements;
    private int totalPages;
  }

  @Getter
  @Builder
  public static class TagResponse {
    private Long tagId;
    private String tagName;
  }

  public static InsightListResponse of(List<Insight> insights, int number, int size, int totalElements, int totalPages) {

    List<ContentResponse> contentResponses = insights.stream()
        .map(insight -> ContentResponse.builder()
            .insightId(insight.getId())
            .title(insight.getTitle())
            .confirmedContent(insight.getInsightPieces().stream()
                .filter(piece -> piece.getCreatedType().equals(InsightGenerationType.INIT))
                .findFirst()
                .map(InsightPiece::getContent)
                .orElse(null))
            .tags(insight.getInsightTags().stream()
                .map(tag -> TagResponse.builder()
                    .tagId(tag.getTagEntity().getId())
                    .tagName(tag.getTagEntity().getTagName())
                    .build())
                .toList())
            .createdDate(insight.getCreatedAt())
            .build())
        .toList();

    PageResponse pageResponse = PageResponse.builder()
        .number(number)
        .size(size)
        .totalElements(totalElements)
        .totalPages(totalPages)
        .build();

    return InsightListResponse.builder()
        .content(contentResponses)
        .page(pageResponse)
        .build();
  }
}
