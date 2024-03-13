package com.sns.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

import com.sns.data.TestMockData;
import com.sns.domain.user.dto.SignupRequestDto;
import com.sns.domain.user.entity.User;
import com.sns.domain.user.repository.UserRepository;
import com.sns.domain.user.service.UserServiceImpl;
import com.sns.global.exception.InvalidInputException;
import com.sns.global.exception.UserNotFoundException;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    /*
     * 유저 정보 조회 메서드는 repository 단에의 로직으로 생략
     *
     * */
    @InjectMocks
    private UserServiceImpl userServiceImpl;
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    Long testUserId;

    @BeforeEach
    void setUp() {
        testUserId = 1L;
    }

    @Nested
    public class userServiceExceptionTest {

        @Test
        @DisplayName("중복된 이메일인 경우, 회원가입 실패 테스트")
        void duplicateEmailTest() {
            SignupRequestDto requestDto = new SignupRequestDto("test@example.com", "testUsername",
                "testPassword");
            given(userRepository.existsByEmail(requestDto.getEmail())).willReturn(true);

            assertThrows(InvalidInputException.class, () -> {
                userServiceImpl.signup(requestDto);
            });
        }

        @Test
        @DisplayName("유저가 존재하지 않을 경우, 단일 유저 정보 조회 실패 테스트")
        void UserNotFoundTest() {
            given(userRepository.searchUserInfo(anyLong())).willReturn(Optional.empty());

            assertThrows(UserNotFoundException.class, () -> {
                userServiceImpl.getUserInfo(testUserId);
            });
        }

        @Test
        @DisplayName("유저가 존재하지 않을경우, 유저 객체를 반환하지 않는 테스트")
        void UserNotFoundTestTwo() {
            given(userRepository.findById(anyLong())).willReturn(Optional.empty());

            assertThrows(UserNotFoundException.class, () -> {
                userServiceImpl.findUser(testUserId);
            });
        }
    }

    @Nested
    public class softDeleteUserInfoTest {

        @Test
        @DisplayName("유저정보 논리 삭제 성공 테스트")
        void deleteUserInfoTest() {
            User testUser = TestMockData.testUser();

            userServiceImpl.deleteUserInfo(testUser);

            assertThat("Y").isEqualTo(testUser.getDeleted_YN());
        }
    }
}
