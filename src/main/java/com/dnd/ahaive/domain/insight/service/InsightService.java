package com.dnd.ahaive.domain.insight.service;

import com.dnd.ahaive.domain.insight.entity.Insight;
import com.dnd.ahaive.domain.insight.exception.InsightAccessDeniedException;
import com.dnd.ahaive.domain.insight.repository.InsightRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class InsightService {

    private final InsightRepository insightRepository;

    @Transactional(readOnly = true)
    public Insight getValidatedInsight(long insightId, String username) {
        Insight insight = insightRepository.findByIdWithUser(insightId)
                .orElseThrow(() -> new EntityNotFoundException("해당 인사이트를 찾을 수 없습니다. insightId : " + insightId));

        if (insight.isNotWrittenBy(username)) {
            throw new InsightAccessDeniedException(
                    "해당 인사이트에 대한 접근 권한이 없습니다. insightId : " + insightId + ", username : " + username);
        }

        return insight;
    }
}
