package com.sns.domain.user.dto;

import com.sns.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserResponseDto {

    private final String email;
    private final String username;

    public UserResponseDto(String email, String username) {
        this.email = email;
        this.username = username;
    }

    public UserResponseDto(User user) {
        this.email = user.getEmail();
        this.username = user.getUsername();
    }
}
