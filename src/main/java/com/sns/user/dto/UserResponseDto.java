package com.sns.user.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserResponseDto {

    private final String email;
    private final String username;

    public UserResponseDto(String email, String nickname) {
        this.email = email;
        this.username = nickname;
    }
}
