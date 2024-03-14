package com.sns.domain.post.aop;

import com.sns.domain.post.repository.PostRepository;
import com.sns.global.exception.PostNotFoundException;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class PostValidationAspect {

    private final PostRepository postRepository;

    @Autowired
    public PostValidationAspect(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Before("@annotation(com.sns.domain.post.aop.ValidatePost) && args(postId,..)")
    public void validatePost(Long postId) {
        postRepository.findById(postId).orElseThrow(
            () -> new PostNotFoundException("해당 게시글이 존재하지 않습니다."));
    }
}
