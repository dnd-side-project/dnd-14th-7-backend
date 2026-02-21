package com.dnd.ahaive.domain.tag.service;

import com.dnd.ahaive.domain.insight.entity.Insight;
import com.dnd.ahaive.domain.insight.service.InsightService;
import com.dnd.ahaive.domain.tag.controller.dto.TagRegisterRequestDto;
import com.dnd.ahaive.domain.tag.entity.TagEntity;
import com.dnd.ahaive.domain.tag.repository.InsightTagRepository;
import com.dnd.ahaive.domain.tag.repository.TagEntityRepository;
import com.dnd.ahaive.domain.user.entity.User;
import com.dnd.ahaive.domain.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TagService {

    private final UserRepository userRepository;
    private final TagEntityRepository tagEntityRepository;
    private final InsightTagRepository insightTagRepository;
    private final InsightService insightService;

    @Transactional
    public void register(TagRegisterRequestDto tagRegisterRequestDto, String uuid) {
        User user = userRepository.findByUserUuid(uuid)
                .orElseThrow(() -> new EntityNotFoundException("해당 회원이 존재하지 않습니다. uuid: " + uuid));

        List<TagEntity> tags = tagEntityRepository.findAllByUser(user);
        String tagName = tagRegisterRequestDto.tagName();

        if (tags.stream().anyMatch(tag -> tag.containsTag(tagName))) {
            throw new IllegalStateException("중복된 태그입니다. tagName: " + tagName);
        }

        // 없으면 추가
        TagEntity tagEntity = TagEntity.of(user, tagName);
        tagEntityRepository.save(tagEntity);
    }

    @Transactional
    public void delete(long insightId, long tagId, String uuid) {
        Insight insight = insightService.getValidatedInsight(insightId, uuid);
        TagEntity tagEntity = tagEntityRepository.findById(tagId)
                .orElseThrow(() -> new EntityNotFoundException("해당 태그가 존재하지 않습니다. tagId: " + tagId));
        User user = tagEntity.getUser();

        if (user.isNotSameUser(uuid)) {
            throw new IllegalStateException("해당 태그는 유저가 가지고 있지 않습니다. tagId : " + tagId + ", userUuid: " + uuid);
        }

        insightTagRepository.deleteByTagEntityIdAndInsightId(tagId, insightId);
    }
}
