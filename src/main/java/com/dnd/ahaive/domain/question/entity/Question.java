package com.dnd.ahaive.domain.question.entity;

import com.dnd.ahaive.domain.insight.entity.Insight;
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
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Question extends BaseEntity {

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    @Column(name = "question_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "insight_id")
    private Insight insight;

    private String content;

    @Enumerated(EnumType.STRING)
    private QuestionStatus status;

    private long version;

    public boolean isNotFrom(Insight insight) {
        return this.insight == insight;
    }

    public void activate() {
        if (this.status != QuestionStatus.ARCHIVED) {
            throw new IllegalStateException("ARCHIVED 질문만 되돌릴 수 있습니다.");
        }
        this.status = QuestionStatus.WAITING;
    }
}
