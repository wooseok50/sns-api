package com.sns.domain.like.service;

import com.sns.domain.like.dto.LikeResponseDto;
import com.sns.domain.like.entity.Like;
import org.springframework.transaction.annotation.Transactional;

public interface LikeService {

    /**
     * 좋아요 생성
     * 게시글이 존재하지 않을 경우 exception 이미 해당 게시글에 좋아요를 생설한 경우 exception
     *
     * @param postId 게시글 Id
     * @param userId 좋아요를 누를 유저 Id
     */
    void createPostLike(Long postId, Long userId);

    /**
     * 좋아요 수 조회
     * 게시글이 존재하지 않을 경우 exception
     *
     * @param postId 게시글 Id
     * @return 좋아요 수
     */
    LikeResponseDto countLikes(Long postId);

    /**
     * 좋아요 삭제
     * 게시글이 존재하지 않을 경우 exception 이미 해당 게시글에 좋아요를 삭제한 경우 exception
     *
     * @param postId 게시글 Id
     * @param userId 좋아요를 삭제할 유저 Id
     */
    void deletePostLike(Long postId, Long userId);

    /**
     * 알림 기능에 사용될 팔로우 객체
     *
     * @param postId 좋아요를 찾을 postId
     * @return 가장 최근에 생성된 좋아요 객체 반환
     */
    Like findLatestLike(Long postId);
}
