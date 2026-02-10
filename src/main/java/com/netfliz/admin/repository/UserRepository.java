package com.netfliz.admin.repository;

import com.netfliz.admin.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByEmail(String email);

    Boolean existsByPhone(String phone);

    Boolean existsByEmail(String email);

    Optional<UserEntity> findByUsername(String username);
}
