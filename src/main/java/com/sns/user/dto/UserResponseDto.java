package com.sns.user.dto;

import com.sns.user.entity.User;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserResponseDto {

    private final String email;
    private final String username;

    public UserResponseDto(User user) {
        this.email = user.getEmail();
        this.username = user.getUsername();
    }
}
