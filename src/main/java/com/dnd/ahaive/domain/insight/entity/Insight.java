package com.dnd.ahaive.domain.insight.entity;

import com.dnd.ahaive.domain.tag.entity.InsightTag;
import com.dnd.ahaive.domain.user.entity.User;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Insight {

    @Id @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;

    private String initThought;
    private String title;

    private int view;

    private boolean trash;

    private LocalDateTime trashedAt;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "insight")
    private List<InsightPiece> insightPieces = new ArrayList<>();

    @OneToMany(mappedBy = "insight")
    private List<InsightTag> insightTags = new ArrayList<>();


    public void changeUser(User user) {
        this.user = user;
    }

    @Builder
    private Insight(String initThought, String title, User user) {
        this.initThought = initThought;
        this.title = title;
        this.user = user;
        this.view = 0;
        this.trash = false;
        this.trashedAt = null;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = this.createdAt;
    }

    public void moveToTrash() {
        this.trash = true;
        this.trashedAt = LocalDateTime.now();
    }

    public void increaseView() {
        this.view++;
    }

    public void refreshUpdatedAt() {
        this.updatedAt = LocalDateTime.now();
    }

    public static Insight from(String initThought, String title, User user) {
        return Insight.builder()
                .initThought(initThought)
                .title(title)
                .user(user)
                .build();
    }

    public boolean isNotWrittenBy(String username) {
        return user.isNotSameUser(username);
    }
}