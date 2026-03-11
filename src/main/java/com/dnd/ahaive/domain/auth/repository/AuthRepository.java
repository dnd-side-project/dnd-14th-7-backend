package com.dnd.ahaive.domain.auth.repository;

import com.dnd.ahaive.domain.auth.entity.Auth;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthRepository extends JpaRepository<Auth, Long> {
  Optional<Auth> findByUserUuid(String userUuid);
  void deleteByUserUuid(String userUuid);
}
