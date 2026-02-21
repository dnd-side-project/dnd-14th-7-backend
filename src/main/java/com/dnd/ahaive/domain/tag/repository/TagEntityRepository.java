package com.dnd.ahaive.domain.tag.repository;

import com.dnd.ahaive.domain.tag.entity.TagEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagEntityRepository extends JpaRepository<TagEntity, Long> {
  List<TagEntity> findAllByUserId(Long userId);
}
