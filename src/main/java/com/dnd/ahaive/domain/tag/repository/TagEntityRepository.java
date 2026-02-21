package com.dnd.ahaive.domain.tag.repository;

import com.dnd.ahaive.domain.tag.entity.TagEntity;
import com.dnd.ahaive.domain.user.entity.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagEntityRepository extends JpaRepository<TagEntity, Long> {

  List<TagEntity> findAllByUser(User user);

  List<TagEntity> findAllByUserId(Long userId);

  Optional<TagEntity> findByIdAndUser(Long tagId, User user);
}
