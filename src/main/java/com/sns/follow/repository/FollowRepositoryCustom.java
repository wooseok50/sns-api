package com.sns.follow.repository;

import com.sns.follow.dto.FollowerResponseDto;
import com.sns.follow.dto.FollowingResponseDto;
import java.util.List;

public interface FollowRepositoryCustom {

    List<FollowingResponseDto> getFollowingList(Long fromUserId);

    List<FollowerResponseDto> getFollowerList(Long toUserId);
}
