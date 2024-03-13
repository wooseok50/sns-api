package com.sns.domain.follow.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sns.domain.follow.dto.FollowerResponseDto;
import com.sns.domain.follow.dto.FollowingResponseDto;
import com.sns.domain.follow.entity.QFollow;
import com.sns.domain.user.entity.QUser;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FollowRepositoryCustomImpl implements FollowRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;
    QFollow follow = QFollow.follow;
    QUser user = QUser.user;

    @Override
    public List<FollowingResponseDto> getFollowingList(Long fromUserId) {

        List<FollowingResponseDto> followingResponseDtoList = jpaQueryFactory
            .select(Projections.constructor(FollowingResponseDto.class, follow, user.username))
            .from(follow)
            .join(user).on(follow.toUserId.eq(user.id))
            .where(follow.fromUserId.eq(fromUserId))
            .fetch();

        return followingResponseDtoList;
    }

    @Override
    public List<FollowerResponseDto> getFollowerList(Long toUserId) {

        List<FollowerResponseDto> followerResponseDtoList = jpaQueryFactory
            .select(Projections.constructor(FollowerResponseDto.class, follow, user.username))
            .from(follow)
            .join(user).on(follow.fromUserId.eq(user.id))
            .where(follow.toUserId.eq(toUserId))
            .fetch();

        return followerResponseDtoList;
    }
}
