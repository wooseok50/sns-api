package com.sns.domain.aop;

import com.sns.domain.post.service.PostService;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class PostValidationAspect {

    private final PostService postService;

    @Autowired
    public PostValidationAspect(PostService postService) {
        this.postService = postService;
    }

    @Before("execution(* com.sns.domain.like.service.LikeService.*(..)) && args(postId,..) && !execution(* com.sns.domain.like.service.LikeService.findLatestLike(..))")
    public void validatePost(Long postId) {
        postService.checkValidatePost(postId);
    }
}
