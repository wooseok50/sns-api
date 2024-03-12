package com.sns.post.service;

import com.sns.global.exception.PostNotFoundException;
import com.sns.post.dto.PostRequestDto;
import com.sns.post.dto.PostResponseDto;
import com.sns.post.entity.Post;
import com.sns.post.repository.PostRepository;
import com.sns.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    @Transactional
    public void createPost(PostRequestDto postRequestDto, User user) {

        Post post = Post.builder()
            .userId(user.getId())
            .username(user.getUsername())
            .title(postRequestDto.getTitle())
            .content(postRequestDto.getContent())
            .deleted_YN("N")
            .build();

        postRepository.save(post);
    }

    @Transactional(readOnly = true)
    public PostResponseDto getPost(Long postId) {

        Post post = findPost(postId);
        PostResponseDto responseDto = new PostResponseDto(post);

        return responseDto;
    }

    private Post findPost(Long postId) {
        return postRepository.findById(postId).orElseThrow(()
            -> new PostNotFoundException("해당 게시글이 존재하지 않습니다."));
    }
}
