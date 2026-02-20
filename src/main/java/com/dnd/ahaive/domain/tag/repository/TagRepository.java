package com.dnd.ahaive.domain.tag.repository;

import com.dnd.ahaive.domain.tag.entity.Tag;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag, Long> {

  List<Tag> findAllByInsightId(Long insightId);
}
