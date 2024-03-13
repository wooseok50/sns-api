package com.sns.domain.post.dto;

import lombok.Getter;

@Getter
public class PostRequestDto {

    private String username;
    private String title;
    private String content;

    public PostRequestDto(String testUsername, String title, String content) {
        this.username = testUsername;
        this.title = title;
        this.content = content;
    }
}
