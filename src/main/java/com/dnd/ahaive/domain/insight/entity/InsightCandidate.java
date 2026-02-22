package com.dnd.ahaive.domain.insight.entity;

import com.dnd.ahaive.global.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
public class InsightCandidate extends BaseEntity {

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    @Column(name = "insight_candidate_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "insight_piece_id")
    private InsightPiece insightPiece;

    private String content;

    private long version;

    public void versionUp() {
        this.version++;
    }

    @Builder
    private InsightCandidate(InsightPiece insightPiece, String content, long version) {
        this.insightPiece = insightPiece;
        this.content = content;
        this.version = version;
    }

    public static InsightCandidate of(InsightPiece insightPiece, String content, long version) {
        return InsightCandidate.builder()
            .insightPiece(insightPiece)
            .content(content)
            .version(version)
            .build();
    }
}
