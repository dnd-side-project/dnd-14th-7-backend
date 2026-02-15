package com.dnd.ahaive.domain.user.repository;

import com.dnd.ahaive.domain.user.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

  Optional<User> findByUserUuid(String userUuid);
  Optional<User> findByProviderId(String providerId);

}
