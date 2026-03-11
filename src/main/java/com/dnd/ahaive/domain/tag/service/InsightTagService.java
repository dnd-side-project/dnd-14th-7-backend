package com.dnd.ahaive.domain.tag.service;

import com.dnd.ahaive.domain.tag.controller.dto.InsightSummary;
import com.dnd.ahaive.domain.tag.controller.dto.InsightSummaryByTag;
import com.dnd.ahaive.domain.tag.controller.dto.TagSummary;
import com.dnd.ahaive.domain.tag.controller.dto.TotalInsightSummaryByTag;
import com.dnd.ahaive.domain.tag.repository.InsightTagRepository;
import com.dnd.ahaive.domain.tag.service.dto.TagInsightCount;
import com.dnd.ahaive.domain.tag.service.dto.TagInsightTitle;
import com.dnd.ahaive.domain.user.entity.User;
import com.dnd.ahaive.domain.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class InsightTagService {

    private final UserRepository userRepository;
    private final InsightTagRepository insightTagRepository;

    @Transactional(readOnly = true)
    public TagSummary getTagsWithInsightCount(String uuid) {
        User user = userRepository.findByUserUuid(uuid)
                .orElseThrow(() -> new EntityNotFoundException("회원을 찾을 수 없습니다. uuid: " + uuid));

        List<TagInsightCount> tagInsightCountsOrderByTagName =
                insightTagRepository.findTagInsightCountsOrderByTagName(user);

        return new TagSummary(tagInsightCountsOrderByTagName);
    }

    @Transactional(readOnly = true)
    public TotalInsightSummaryByTag getInsightsGroupByTag(String uuid) {
        User user = userRepository.findByUserUuid(uuid)
                .orElseThrow(() -> new EntityNotFoundException("회원을 찾을 수 없습니다. uuid: " + uuid));

        // 태그에 해당하는 인사이트 개수
        List<TagInsightCount> counts = insightTagRepository.countInsightsByTag(user);
        // 태그에 해당하는 인사이트 제목 3개
        List<TagInsightTitle> titles = insightTagRepository.findInsightTitlesByTagLatest(user);

        int previewLimit = 3;

        Map<Long, List<InsightSummary>> previewMap = new LinkedHashMap<>();
        for (TagInsightTitle row : titles) {
            previewMap.putIfAbsent(row.tagId(), new ArrayList<>());
            List<InsightSummary> list = previewMap.get(row.tagId());
            if (list.size() < previewLimit) {
                list.add(new InsightSummary(row.insightTitle()));
            }
        }

        List<InsightSummaryByTag> content = counts.stream()
                .map(c -> new InsightSummaryByTag(
                        c.tagId(),
                        c.tagName(),
                        previewMap.getOrDefault(c.tagId(), List.of()),
                        c.insightCount()
                ))
                .toList();

        return new TotalInsightSummaryByTag(content);
    }
}
