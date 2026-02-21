package com.dnd.ahaive.domain.insight.entity;

import com.dnd.ahaive.global.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
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
public class InsightPiece extends BaseEntity {

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    @Column(name = "insight_piece_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "insight_id")
    private Insight insight;

    private String content;

    @Enumerated(EnumType.STRING)
    private InsightGenerationType createdType;

    @Builder
    private InsightPiece(Insight insight, String content, InsightGenerationType createdType) {
        this.insight = insight;
        this.content = content;
        this.createdType = createdType;
    }

    public void updateContent(String content) {
        this.content = content;
    }

    public static InsightPiece of(Insight insight, String content, InsightGenerationType createdType) {
        return InsightPiece.builder()
            .insight(insight)
            .content(content)
            .createdType(createdType)
            .build();
    }


}
