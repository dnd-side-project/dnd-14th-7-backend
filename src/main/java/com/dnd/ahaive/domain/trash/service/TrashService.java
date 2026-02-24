package com.dnd.ahaive.domain.trash.service;

import com.dnd.ahaive.domain.insight.entity.Insight;
import com.dnd.ahaive.domain.insight.exception.InsightNotFoundException;
import com.dnd.ahaive.domain.insight.repository.InsightRepository;
import com.dnd.ahaive.domain.trash.dto.response.TrashListResponse;
import com.dnd.ahaive.domain.user.entity.User;
import com.dnd.ahaive.domain.user.repository.UserRepository;
import com.dnd.ahaive.global.exception.ErrorCode;
import com.dnd.ahaive.global.security.exception.UserNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TrashService {

  private final UserRepository userRepository;
  private final InsightRepository insightRepository;


  public TrashListResponse getTrashInsights(String uuid) {

    User user = userRepository.findByUserUuid(uuid).orElseThrow(
        () -> new UserNotFoundException(ErrorCode.USER_NOT_FOUND)
    );

    List<Insight> insights = insightRepository.findAllTrashByUserIdWithPiecesAndTags(user.getId());

    return TrashListResponse.from(insights);
  }
}
