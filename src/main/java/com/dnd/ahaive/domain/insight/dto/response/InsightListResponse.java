package com.dnd.ahaive.domain.insight.dto.response;

import com.dnd.ahaive.domain.insight.entity.Insight;
import com.dnd.ahaive.domain.tag.entity.TagEntity;
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

  public static InsightListResponse of(Insight insight, )





}
