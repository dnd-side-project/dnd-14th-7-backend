package com.dnd.ahaive.domain.user.service;

import com.dnd.ahaive.domain.user.dto.response.UserResponse;
import com.dnd.ahaive.domain.user.entity.Position;
import com.dnd.ahaive.domain.user.entity.User;
import com.dnd.ahaive.domain.user.repository.UserRepository;
import com.dnd.ahaive.global.exception.ErrorCode;
import com.dnd.ahaive.global.security.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;

  public UserResponse getUserInfo(String uuid) {
    User user = userRepository.findByUserUuid(uuid).orElseThrow(
        () -> new UserNotFoundException(ErrorCode.USER_NOT_FOUND)
    );

    return UserResponse.from(user);
  }

  @Transactional
  public void updateUserPosition(String uuid, Position position) {
    User user = userRepository.findByUserUuid(uuid).orElseThrow(
        () -> new UserNotFoundException(ErrorCode.USER_NOT_FOUND)
    );

    user.updatePosition(position);
  }
}
