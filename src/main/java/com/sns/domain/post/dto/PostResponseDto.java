package com.sns.domain.post.dto;

import com.sns.domain.post.entity.Post;
import lombok.Getter;

@Getter
public class PostResponseDto {

    private Long postId;
    private Long userId;
    private String username;
    private String title;
    private String content;

    public PostResponseDto(Post post) {
        this.postId = post.getId();
        this.userId = post.getUserId();
        this.username = post.getUsername();
        this.title = post.getTitle();
        this.content = post.getContent();
    }
}
