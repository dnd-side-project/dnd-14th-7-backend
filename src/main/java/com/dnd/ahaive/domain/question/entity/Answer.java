package com.dnd.ahaive.domain.question.entity;

import com.dnd.ahaive.global.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Answer extends BaseEntity {

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    @Column(name = "answer_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "questions_id", unique = true)
    private Question question;

    private String content;

    private boolean isConverted;

    @Builder
    private Answer(Question question, String content, boolean isConverted) {
        this.question = question;
        this.content = content;
        this.isConverted = isConverted;
    }

    public static Answer of(Question question, String content, boolean isConverted) {
        return Answer.builder().question(question).content(content).isConverted(isConverted).build();
    }

    public void convert() {
        this.isConverted = true;
    }
}
