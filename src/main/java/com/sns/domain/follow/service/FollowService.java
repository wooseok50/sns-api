package com.sns.domain.follow.service;

import com.sns.domain.follow.dto.FollowerResponseDto;
import com.sns.domain.follow.dto.FollowingResponseDto;
import com.sns.domain.follow.entity.Follow;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

public interface FollowService {

    /**
     * 팔로우 생성
     * 본인을 팔로우할 경우 exception 팔로우할 대상이 존재하지 않을 경우 exception
     *
     * @param fromUserId 유저 Id
     * @param toUserId   팔로우할 유저 Id
     */
    void createFollow(Long fromUserId, Long toUserId);

    /**
     * 선택한 유저의 팔로잉 목록 조회
     *
     * @param fromUserId 유저 Id
     * @return 유저의 Id와 username 정보 리스트
     */
    List<FollowingResponseDto> getFollowingList(Long fromUserId);

    /**
     * 선택한 유저의 팔로워 목록 조회
     *
     * @param toUserId 유저 Id
     * @return 유저의 Id와 username 정보 리스트
     */
    List<FollowerResponseDto> getFollowerList(Long toUserId);

    /**
     * 팔로우 삭제
     * 삭제할 팔로우할 대상이 존재하지 않을 경우 exception
     *
     * @param fromUserId 유저 Id
     * @param toUserId   팔로우 삭제할 유저 Id
     */
    void deleteFollow(Long fromUserId, Long toUserId);

    /**
     * 알림 기능에 사용될 팔로우 객체
     *
     * @param toUserId follow 찾을 userId
     * @return 가장 최근에 생성된 follow 객체 반환
     */
    Follow findLatestUser(Long toUserId);
}
