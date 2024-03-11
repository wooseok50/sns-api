package com.sns.user.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sns.user.entity.QUser;
import com.sns.user.entity.User;
import java.util.Optional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SearchUserImpl implements SearchUser {

    private final JPAQueryFactory jpaQueryFactory;
    QUser user = QUser.user;

    @Override
    public Optional<User> searchUserInfo(Long userId) {
        return Optional.ofNullable(jpaQueryFactory.selectFrom(user)
            .where(user.id.eq(userId)
                .and(user.deleted_YN.eq("N")))
            .fetchOne());
    }
}
