package com.dnd.ahaive.domain.search.service;

import com.dnd.ahaive.domain.insight.entity.Insight;
import com.dnd.ahaive.domain.insight.entity.InsightGenerationType;
import com.dnd.ahaive.domain.insight.entity.InsightPiece;
import com.dnd.ahaive.domain.insight.repository.InsightPieceRepository;
import com.dnd.ahaive.domain.insight.repository.InsightRepository;
import com.dnd.ahaive.domain.search.controller.dto.SearchResultDto;
import com.dnd.ahaive.domain.search.service.dto.InsightSearchDto;
import com.dnd.ahaive.domain.search.service.dto.TagSearchDto;
import com.dnd.ahaive.domain.tag.repository.TagEntityRepository;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final TagEntityRepository tagEntityRepository;
    private final InsightRepository insightRepository;
    private final InsightPieceRepository insightPieceRepository;

    @Transactional
    public SearchResultDto search(String uuid, String searchTerm) {
        List<TagSearchDto> tags = tagEntityRepository.searchTags(uuid, searchTerm);

        List<Insight> insights = insightRepository.searchInsights(
                uuid, searchTerm, InsightGenerationType.INIT, PageRequest.of(0, 20));

        List<Long> insightIds = insights.stream().map(Insight::getId).toList();
        Map<Long, String> initPieceMap = insightPieceRepository.findInitPiecesByInsightIds
                        (insightIds, InsightGenerationType.INIT)
                .stream()
                .collect(Collectors.toMap(
                        ip -> ip.getInsight().getId(),
                        InsightPiece::getContent
                ));

        List<InsightSearchDto> insightDtos = insights.stream()
                .map(i -> InsightSearchDto.of(i, initPieceMap.get(i.getId())))
                .toList();

        return new SearchResultDto(tags, insightDtos);
    }
}
