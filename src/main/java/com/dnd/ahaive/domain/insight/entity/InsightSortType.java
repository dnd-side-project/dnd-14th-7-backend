package com.dnd.ahaive.domain.insight.entity;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum InsightSortType {
  LATEST, VIEWS;

  @JsonCreator
  public static InsightSortType from(String value) {
    return InsightSortType.valueOf(value.toUpperCase());
  }
}
