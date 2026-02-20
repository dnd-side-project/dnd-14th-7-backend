package com.dnd.ahaive.domain.insight.repository;

import com.dnd.ahaive.domain.insight.entity.Insight;
import com.dnd.ahaive.domain.user.entity.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InsightRepository extends JpaRepository<Insight, Long> {

  List<Insight> findByUserAndTrash(User user, boolean trash);

}
