package com.sns.domain.user.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sns.domain.user.entity.QUser;
import com.sns.domain.user.entity.User;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@RequiredArgsConstructor
public class UserRepositoryCustomImpl implements UserRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;
    QUser user = QUser.user;

    @Override
    public Optional<User> searchUserInfo(Long userId) {
        return Optional.ofNullable(jpaQueryFactory.selectFrom(user)
            .where(user.id.eq(userId)
                .and(user.deleted_YN.eq("N")))
            .fetchOne());
    }

    @Override
    public Page<User> searchAllUserInfo(Pageable pageable) {

        List<User> users = jpaQueryFactory
            .selectFrom(user)
            .where(user.deleted_YN.eq("N"))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        long total = jpaQueryFactory
            .selectFrom(user)
            .where(user.deleted_YN.eq("N"))
            .fetch().size();

        return new PageImpl<>(users, pageable, total);
    }
}
