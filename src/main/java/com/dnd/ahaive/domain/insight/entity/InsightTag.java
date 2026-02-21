package com.dnd.ahaive.domain.insight.entity;

import com.dnd.ahaive.domain.tag.entity.TagEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class InsightTag {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "insight_id")
  private Insight insight;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "tag_entity_id")
  private TagEntity tagEntity;

  @Builder
  private InsightTag(Insight insight, TagEntity tagEntity) {
    this.insight = insight;
    this.tagEntity = tagEntity;
  }

  public static InsightTag of(Insight insight, TagEntity tagEntity) {
    return InsightTag.builder()
        .insight(insight)
        .tagEntity(tagEntity)
        .build();
  }

}
