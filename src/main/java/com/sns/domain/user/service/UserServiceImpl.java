package com.sns.domain.user.service;

import com.sns.domain.user.dto.SignupRequestDto;
import com.sns.domain.user.dto.UserResponseDto;
import com.sns.domain.user.entity.User;
import com.sns.domain.user.entity.UserRole;
import com.sns.domain.user.repository.UserRepository;
import com.sns.global.exception.InvalidInputException;
import com.sns.global.exception.UserNotFoundException;
import com.sns.global.jwt.entity.RefreshTokenEntity;
import com.sns.global.jwt.repository.TokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
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

    @Override
    @Transactional
    public void logout(Long userId) {

        RefreshTokenEntity refreshToken = tokenRepository.findByUserId(userId);

        tokenRepository.deleteToken(refreshToken);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponseDto getUserInfo(Long userId) {

        User user = userRepository.searchUserInfo(userId)
            .orElseThrow(() -> new UserNotFoundException("해당 유저를 찾을 수 없습니다."));

        return UserResponseDto.builder()
            .email(user.getEmail())
            .username(user.getUsername())
            .build();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserResponseDto> getAllUserInfo(int page, int size) {

        Sort sort = Sort.by(Sort.Direction.ASC, "username");
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<User> users = userRepository.searchAllUserInfo(pageable);

        return users.map(UserResponseDto::new);
    }

    @Override
    @Transactional
    public void deleteUserInfo(User user) {
        user.softDelete();
    }

    @Override
    public void checkValidateUser(Long userId) {
        userRepository.findById(userId).orElseThrow(
            () -> new UserNotFoundException("해당 유저가 존재하지 않습니다."));
    }


    @Override
    public User findUser(Long userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new UserNotFoundException("해당 User는 존재하지 않습니다."));
    }
}
