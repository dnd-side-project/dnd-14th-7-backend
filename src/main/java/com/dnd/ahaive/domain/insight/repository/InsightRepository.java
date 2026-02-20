package com.dnd.ahaive.domain.insight.repository;

import com.dnd.ahaive.domain.insight.entity.Insight;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InsightRepository extends JpaRepository<Insight, Long> {
}
