package com.dnd.ahaive.domain.tag.repository;

import com.dnd.ahaive.domain.search.service.dto.TagSearchDto;
import com.dnd.ahaive.domain.tag.entity.TagEntity;
import com.dnd.ahaive.domain.user.entity.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TagEntityRepository extends JpaRepository<TagEntity, Long> {

  List<TagEntity> findAllByUser(User user);

  List<TagEntity> findAllByUserId(Long userId);

  Optional<TagEntity> findByIdAndUser(Long tagId, User user);

  @Query("SELECT new com.dnd.ahaive.domain.search.service.dto.TagSearchDto(t.id, t.tagName, COUNT(it.id)) " +
          "FROM Tag t JOIN InsightTag it ON it.tagEntity = t " +
          "WHERE t.user.userUuid = :uuid " +
          "AND t.tagName LIKE %:searchTerm% " +
          "GROUP BY t.id, t.tagName")
  List<TagSearchDto> searchTags(@Param("uuid") String uuid,
                                @Param("searchTerm") String searchTerm);
}
