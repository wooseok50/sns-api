package com.sns.like.dto;

import lombok.Getter;

@Getter
public class LikeResponseDto {

    private Long likeCount;

    public LikeResponseDto(Long likeCount) {
        this.likeCount = likeCount;
    }
}
