package com.sns.global.jwt.repository;

import com.sns.global.jwt.entity.RefreshTokenEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class TokenRepositoryImpl implements TokenRepository {

    private final RefreshTokenJpaRepository refreshTokenJpaRepository;

    @Override
    public void register(Long userId, String token) {
        RefreshTokenEntity entity = RefreshTokenEntity.of(userId, token);
        refreshTokenJpaRepository.save(entity);
    }

    @Override
    public RefreshTokenEntity findByUserId(Long userId) {
        return refreshTokenJpaRepository.findByUserId(userId).orElseThrow(
            () -> new IllegalArgumentException("존재하지 않는 토근입니다.")
        );
    }

    @Override
    public void deleteToken(RefreshTokenEntity token) {
        refreshTokenJpaRepository.deleteByToken(token.getToken());
    }
}
