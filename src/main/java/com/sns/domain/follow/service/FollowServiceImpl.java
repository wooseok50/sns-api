package com.sns.domain.follow.service;

import com.sns.domain.follow.dto.FollowerResponseDto;
import com.sns.domain.follow.dto.FollowingResponseDto;
import com.sns.domain.follow.entity.Follow;
import com.sns.domain.follow.repository.FollowRepository;
import com.sns.global.exception.InvalidInputException;
import com.sns.domain.user.service.UserService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FollowServiceImpl implements FollowService {

    private final FollowRepository followRepository;
    private final UserService userService;

    @Override
    @Transactional
    public void createFollow(Long fromUserId, Long toUserId) {

        if (fromUserId.equals(toUserId)) {
            throw new InvalidInputException("자신을 팔로우할 수 없습니다.");
        }

        userService.throwExceptionIfUserNotFound(toUserId);

        Follow follow = Follow.builder()
            .fromUserId(fromUserId)
            .toUserId(toUserId)
            .build();

        followRepository.save(follow);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FollowingResponseDto> getFollowingList(Long fromUserId) {
        return followRepository.getFollowingList(fromUserId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FollowerResponseDto> getFollowerList(Long toUserId) {
        return followRepository.getFollowerList(toUserId);
    }

    @Override
    @Transactional
    public void deleteFollow(Long fromUserId, Long toUserId) {

        userService.throwExceptionIfUserNotFound(toUserId);

        Follow follow = followRepository.findByFromUserIdAndToUserId(fromUserId, toUserId)
            .orElseThrow(() -> new InvalidInputException("해당 팔로우를 찾을 수 없습니다."));

        followRepository.delete(follow);
    }

    @Override
    public Follow findLatestUser(Long toUserId) {
        return followRepository.findFirstByToUserIdOrderByCreatedAtDesc(toUserId)
            .orElseThrow(() -> new IllegalArgumentException("팔로우를 찾을 수 없습니다."));
    }
}
