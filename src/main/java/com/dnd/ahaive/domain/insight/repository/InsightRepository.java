package com.dnd.ahaive.domain.insight.repository;

import com.dnd.ahaive.domain.insight.entity.Insight;
import com.dnd.ahaive.domain.user.entity.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface InsightRepository extends JpaRepository<Insight, Long> {

    @Query("select i from Insight i join fetch i.user where i.id = :insightId")
    Optional<Insight> findByIdWithUser(@Param("insightId") long insightId);

    List<Insight> findByUserAndTrash(User user, boolean trash);

}
