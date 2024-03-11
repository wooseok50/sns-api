package com.sns.user.service;

import com.sns.global.exception.InvalidInputException;
import com.sns.global.jwt.entity.RefreshTokenEntity;
import com.sns.global.jwt.repository.TokenRepository;
import com.sns.user.dto.SignupRequestDto;
import com.sns.user.entity.User;
import com.sns.user.entity.UserRole;
import com.sns.user.repository.UserRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void signup(SignupRequestDto requestDto) {

        String password = passwordEncoder.encode(requestDto.getPassword());

        User user = User.builder()
            .email(requestDto.getEmail())
            .username(requestDto.getUsername())
            .password(password)
            .role(UserRole.USER)
            .deleted_YN("N")
            .build();

        // 회원 중복 확인
        Optional<User> checkUserEmail = userRepository.findByEmail(user.getEmail());
        if (checkUserEmail.isPresent()) {
            throw new InvalidInputException("중복된 email 입니다.");
        }

        userRepository.save(user);
    }

    @Transactional
    public void logout(UserDetails userDetails) {

        User user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow(() ->
            new IllegalArgumentException("User가 존재하지 않습니다.")
        );

        RefreshTokenEntity refreshToken = tokenRepository.findByUserId(user.getId());
        tokenRepository.deleteToken(refreshToken);
    }
}
