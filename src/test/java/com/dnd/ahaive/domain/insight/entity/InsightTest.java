package com.dnd.ahaive.domain.insight.entity;

import static org.assertj.core.api.Assertions.assertThat;

import com.dnd.ahaive.domain.user.entity.Provider;
import com.dnd.ahaive.domain.user.entity.User;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class InsightTest {

  @Test
  @DisplayName("인사이트를 휴지통으로 이동시키면 trash가 true로 변경되고 trashedAt이 현재 시간으로 설정됨.")
  void moveToTrash() {
    // given
    User user = User.createMember(
        "tester",
        100,
        "test@gmail.com",
        Provider.GOOGLE,
        "tester123"
    );

    Insight insight = Insight.from("초기 생각", "인사이트 제목", user);
    LocalDateTime beforeMove = LocalDateTime.now();

    // when
    insight.moveToTrash();

    //then
    assertThat(insight.isTrash()).isTrue();
    assertThat(insight.getTrashedAt()).isNotNull();
    assertThat(insight.getTrashedAt()).isAfterOrEqualTo(beforeMove);
  }

  @Test
  @DisplayName("조회수를 증가시키면 view가 1 증가함.")
  void increaseView() {
    // given
    User user = User.createMember(
        "tester",
        100,
        "test@gmail.com",
        Provider.GOOGLE,
        "tester123"
    );

    Insight insight = Insight.from("초기 생각", "인사이트 제목", user);
    int initView = insight.getView();

    // when
    insight.increaseView();

    // then
    assertThat(insight.getView()).isEqualTo(initView + 1);
  }
}