package com.sns.post.dto;

import com.sns.post.entity.Post;
import lombok.Getter;

@Getter
public class PostResponseDto {

    private Long id;
    private String username;
    private String title;
    private String content;

    public PostResponseDto(Post post) {
        this.id = post.getId();
        this.username = post.getUsername();
        this.title = post.getTitle();
        this.content = post.getContent();
    }
}
