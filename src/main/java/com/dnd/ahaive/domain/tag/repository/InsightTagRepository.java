package com.dnd.ahaive.domain.tag.repository;

import com.dnd.ahaive.domain.tag.entity.InsightTag;
import java.util.List;
import com.dnd.ahaive.domain.tag.service.dto.TagInsightCount;
import com.dnd.ahaive.domain.tag.service.dto.TagInsightTitle;
import com.dnd.ahaive.domain.user.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface InsightTagRepository extends JpaRepository<InsightTag, Long> {

    void deleteByTagEntityIdAndInsightId(Long tagEntityId, Long insightId);

    boolean existsByInsightIdAndTagEntityId(long insightId, long tagEntityId);

    Optional<InsightTag> findByInsightIdAndTagEntityId(Long insightId, Long tagId);

    @Query("select it from InsightTag it join fetch it.tagEntity where it.insight.id = :insightId")
    List<InsightTag> findAllByInsightId(@Param("insightId") Long insightId);
    @Query(""" 
        select new com.dnd.ahaive.domain.tag.service.dto.TagInsightCount(
            t.id,
            t.tagName,
            count(it.id)
        )
        from InsightTag it
            join it.tagEntity t
        where t.user = :user
        group by t.id, t.tagName
        order by count(it.id) desc, t.id asc
    """)
    List<TagInsightCount> countInsightsByTag(User user);

    @Query("""
        select new com.dnd.ahaive.domain.tag.service.dto.TagInsightTitle(
            t.id,
            t.tagName,
            i.title
        )
        from InsightTag it
        join it.tagEntity t
        join it.insight i
        where t.user = :user
        order by t.id asc, i.createdAt desc
    """)
    List<TagInsightTitle> findInsightTitlesByTagLatest(User user);

}
