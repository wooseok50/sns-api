package com.sns.global.jwt.repository;

import com.sns.global.jwt.entity.RefreshTokenEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenJpaRepository extends JpaRepository<RefreshTokenEntity, Long> {

    Optional<RefreshTokenEntity> findByUserId(Long id);

    void deleteByToken(String token);
}
