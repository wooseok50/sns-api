package com.sns.global.jwt.repository;

import com.sns.global.jwt.entity.RefreshTokenEntity;

public interface TokenRepository {

    void register(Long userId, String token);

    RefreshTokenEntity findByUserId(Long userId);

    void deleteToken(RefreshTokenEntity token);
}
