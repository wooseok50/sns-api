package com.sns.user.service;

import com.sns.global.exception.InvalidInputException;
import com.sns.global.exception.UserNotFoundException;
import com.sns.global.jwt.entity.RefreshTokenEntity;
import com.sns.global.jwt.repository.TokenRepository;
import com.sns.user.dto.SignupRequestDto;
import com.sns.user.dto.UserResponseDto;
import com.sns.user.entity.User;
import com.sns.user.entity.UserRole;
import com.sns.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void signup(SignupRequestDto requestDto) {

        if (userRepository.existsByEmail(requestDto.getEmail())) {
            throw new InvalidInputException("중복된 email 입니다.");
        }

        User user = User.builder()
            .email(requestDto.getEmail())
            .username(requestDto.getUsername())
            .password(passwordEncoder.encode(requestDto.getPassword()))
            .role(UserRole.USER)
            .deleted_YN("N")
            .build();

        userRepository.save(user);
    }

    @Transactional
    public void logout(UserDetails userDetails) {

        User user = userRepository.findByEmail(userDetails.getUsername())
            .orElseThrow(() -> new UserNotFoundException("해당 유저가 존재하지 않습니다."));

        RefreshTokenEntity refreshToken = tokenRepository.findByUserId(user.getId());

        tokenRepository.deleteToken(refreshToken);
    }


    public UserResponseDto getUserInfo(Long userId) {

        User user = userRepository.findById(userId)
            .orElseThrow(() -> new UserNotFoundException("해당 유저를 찾을 수 없습니다."));

        return UserResponseDto.builder()
            .email(user.getEmail())
            .username(user.getUsername())
            .build();
    }
}