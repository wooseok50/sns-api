package com.sns.domain.post.service;

import com.sns.domain.post.dto.PostRequestDto;
import com.sns.domain.post.dto.PostResponseDto;
import com.sns.domain.post.entity.Post;
import com.sns.domain.post.repository.PostRepository;
import com.sns.domain.user.entity.User;
import com.sns.global.exception.InvalidInputException;
import com.sns.global.exception.PostNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;

    @Override
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

    @Override
    @Transactional(readOnly = true)
    public PostResponseDto getPost(Long postId) {

        Post post = postRepository.searchPost(postId).orElseThrow(()
            -> new PostNotFoundException("해당 게시글이 존재하지 않습니다."));

        PostResponseDto responseDto = new PostResponseDto(post);

        return responseDto;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PostResponseDto> getPostsByOptions(String title, String username, int page,
        int size) {

        Sort sort = Sort.by(Sort.Direction.ASC, "created_at");
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Post> posts = postRepository.findBySearchOption(title, username, pageable);

        return posts.map(PostResponseDto::new);
    }

    @Override
    @Transactional
    public void updatePost(Long postId, Long userId, PostRequestDto postRequestDto) {

        Post post = findPost(postId);

        if (!post.getUserId().equals(userId)) {
            throw new InvalidInputException("자신의 게시글만 수정할 수 있습니다.");
        }

        post.updatePost(postRequestDto);
    }

    @Override
    @Transactional
    public void deletePost(Long postId, Long userId) {

        Post post = findPost(postId);

        if (!post.getUserId().equals(userId)) {
            throw new InvalidInputException("자신의 게시글만 삭제할 수 있습니다.");
        }

        post.softDelete();
    }

    @Override
    public Post findPost(Long postId) {
        return postRepository.findById(postId).orElseThrow(
            () -> new PostNotFoundException("해당 게시글이 존재하지 않습니다."));
    }

    @Override
    public void checkValidatePost(Long postId) {
        postRepository.findById(postId).orElseThrow(
            () -> new PostNotFoundException("해당 게시글이 존재하지 않습니다."));
    }
}
