package com.sns.domain.post.service;

import com.sns.domain.post.dto.PostRequestDto;
import com.sns.domain.post.dto.PostResponseDto;
import com.sns.domain.post.entity.Post;
import com.sns.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

public interface PostService {

    /**
     * 게시글 등록
     *
     * @param postRequestDto 등록할 게시글의 title, content, username
     * @param user 게시글을 등록할 유저
     */
    void createPost(PostRequestDto postRequestDto, User user);

    /**
     * 선택한 게시글 조회
     * 삭제된(soft delete) 게시글을 제외한 조회
     *
     * @param postId 조회할 게시글 Id
     * @return 게시글 정보
     */
    PostResponseDto getPost(Long postId);

    /**
     * 전체 게시글 조회
     * 삭제된(soft delete) 게시글을 제외한 조회
     *
     * @param title    조회할 게시글의 title (contains)
     * @param username 조회할 게시글의 username (eq)
     * @param page     page
     * @param size     size
     * @return 게시글 정보 (Page)
     */
    Page<PostResponseDto> getPostsByOptions(String title, String username, int page,
        int size);

    /**
     * 게시글 수정
     * 본인이 작성한 게시글일 경우 수정 본인이 작성한 게시글이 아닐 경우 exception
     *
     * @param postId 수정할 게시글의 Id
     * @param userId 게시글의 userId
     * @param postRequestDto page
     */
    void updatePost(Long postId, Long userId, PostRequestDto postRequestDto);

    /**
     * 게시글 삭제
     * 본인이 작성한 게시글일 경우 게시글의 deleted_YN 필드의 값을 "Y"로 할당
     * 본인이 작성한 게시글이 아닐 경우 exception
     *
     * @param postId 수정할 게시글의 Id
     * @param userId 게시글의 userId
     */
    void deletePost(Long postId, Long userId);

    /**
     * 게시글 객체 반환
     * 유저가 존재하지 않을 경우 exception
     *
     * @param postId 조회할 유저 ID
     * @return userId로 조회한 유저 객체
     */
    Post findPost(Long postId);;
}
