package com.hanghae.navis.user.repository;

import com.hanghae.navis.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByKakaoIdOrUsername(Long id, String username);
    Optional<User> findByEmail(String email);
    Optional<User> findByNickname(String nickname);
}
