package com.dnd.ahaive.domain.insight.repository;

import com.dnd.ahaive.domain.insight.entity.Insight;
import com.dnd.ahaive.domain.insight.entity.InsightGenerationType;
import com.dnd.ahaive.domain.user.entity.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface InsightRepository extends JpaRepository<Insight, Long> {

    @Query("select i from Insight i join fetch i.user where i.id = :insightId")
    Optional<Insight> findByIdWithUser(@Param("insightId") long insightId);

    List<Insight> findByUserAndTrash(User user, boolean trash);

    // 태그별 인사이트 전체 조회
    @Query("select distinct i from Insight i "
        + "join fetch i.insightPieces "
        + "join fetch i.insightTags it "
        + "join fetch it.tagEntity "
        + "where i.user.id = :userId and i.trash = false "
        + "and i.id in (select i2.id from Insight i2 join i2.insightTags it2 where it2.tagEntity.id = :tagId)")
    List<Insight> findAllByUserIdAndTagIdWithPiecesAndTags(@Param("userId") long userId, @Param("tagId") long tagId, Pageable pageable);

    // 인사이트 전체 조회
    @Query("select distinct i from Insight i "
        + "join fetch i.insightPieces "
        + "join fetch i.insightTags it "
        + "join fetch it.tagEntity "
        + "where i.user.id = :userId and i.trash = false")
    List<Insight> findAllByUserIdWithPiecesAndTags(@Param("userId") long userId, Pageable pageable);

    // 인사이트(휴지통) 전체 조회
    @Query("select distinct i from Insight i "
        + "join fetch i.insightPieces "
        + "join fetch i.insightTags it "
        + "join fetch it.tagEntity "
        + "where i.user.id = :userId and i.trash = true "
        + "order by i.trashedAt desc")
    List<Insight> findAllTrashByUserIdWithPiecesAndTags(@Param("userId") long userId);


    // 태그별 인사이트 전체 조회 카운트
    @Query("select count(i) from Insight i where i.user.id = :userId and i.trash = false")
    int countByUserId(@Param("userId") long userId);

    // 인사이트 전체 조회 카운트
    @Query("select count(i) from Insight i join i.insightTags it where i.user.id = :userId and i.trash = false and it.tagEntity.id = :tagId")
    int countByUserIdAndTagId(@Param("userId") long userId, @Param("tagId") long tagId);

    @Query("SELECT DISTINCT i FROM Insight i " +
            "JOIN InsightPiece ip ON ip.insight = i " +
            "WHERE i.user.userUuid = :uuid " +
            "AND i.trash = false " +
            "AND (i.title LIKE %:searchTerm% " +
            "OR i.initThought LIKE %:searchTerm% " +
            "OR (ip.createdType = :generationType AND ip.content LIKE %:searchTerm%)) " +
            "ORDER BY i.createdAt DESC")
    List<Insight> searchInsights(@Param("uuid") String uuid,
                                 @Param("searchTerm") String searchTerm,
                                 @Param("generationType") InsightGenerationType generationType,
                                 Pageable pageable);
}
