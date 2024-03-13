package com.sns.domain.follow.repository;

import com.sns.domain.follow.dto.FollowerResponseDto;
import com.sns.domain.follow.dto.FollowingResponseDto;
import java.util.List;

public interface FollowRepositoryCustom {

    List<FollowingResponseDto> getFollowingList(Long fromUserId);

    List<FollowerResponseDto> getFollowerList(Long toUserId);
}
