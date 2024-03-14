package com.sns.domain.like.service;

import com.sns.domain.like.dto.LikeResponseDto;
import com.sns.domain.like.entity.Like;
import com.sns.domain.like.repository.LikeRepository;
import com.sns.domain.post.service.PostService;
import com.sns.global.exception.InvalidInputException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LikeServiceImpl implements LikeService {

    private final LikeRepository likeRepository;
    private final PostService postService;

    @Override
    @Transactional
    public void createPostLike(Long postId, Long userId) {

         // postService.checkValidatePost(postId);

        if (likeRepository.findLikeByPostIdAndUserId(postId, userId).isPresent()) {
            throw new InvalidInputException("이미 해당 게시물에 좋아요를 눌렀습니다.");
        }
        Like like = new Like(postId, userId);

        likeRepository.save(like);
    }

    @Override
    @Transactional(readOnly = true)
    public LikeResponseDto countLikes(Long postId) {

       // postService.checkValidatePost(postId);

        Long likeCount = likeRepository.countByPostId(postId).orElse(0L);

        return new LikeResponseDto(likeCount);
    }

    @Override
    @Transactional
    public void deletePostLike(Long postId, Long userId) {

        //postService.checkValidatePost(postId);

        Like like = likeRepository.findLikeByPostIdAndUserId(postId, userId).orElseThrow(
            () -> new InvalidInputException("이미 해당 게시물의 좋아요를 취소한 상태입니다.")
        );

        likeRepository.delete(like);
    }

    @Override
    public Like findLatestLike(Long postId) {
        return likeRepository.findFirstByPostIdOrderByCreatedAtDesc(postId)
            .orElseThrow(() -> new IllegalArgumentException("좋아요를 찾을 수 없습니다."));
    }
}
