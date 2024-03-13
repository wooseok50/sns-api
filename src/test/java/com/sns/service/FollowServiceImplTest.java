package com.sns.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

import com.sns.data.TestMockData;
import com.sns.domain.follow.dto.FollowerResponseDto;
import com.sns.domain.follow.dto.FollowingResponseDto;
import com.sns.domain.follow.entity.Follow;
import com.sns.domain.follow.repository.FollowRepository;
import com.sns.domain.follow.service.FollowServiceImpl;
import com.sns.domain.user.entity.User;
import com.sns.global.exception.InvalidInputException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FollowServiceImplTest {

    @InjectMocks
    private FollowServiceImpl followServiceImpl;
    @Mock
    private FollowRepository followRepository;

    Long testFromUserId;
    Long testToUserId;

    @BeforeEach
    void setUp() {
        testFromUserId = 1L;
        testToUserId = 2L;
    }

    @Nested
    class createFollowTest {

        @Test
        @DisplayName("본인을 팔로우할 경우 실패 테스트")
        void followMySelfTest() {

            testToUserId = 1L;

            assertThrows(InvalidInputException.class, () -> {
                followServiceImpl.createFollow(testFromUserId, testToUserId);
            });
        }

        @Test
        @DisplayName("팔로우 생성 성공 테스트")
        void createFollowSuccessTest() {

            followServiceImpl.createFollow(testFromUserId, testToUserId);

            then(followRepository).should(times(1)).save(any(Follow.class));
        }
    }

    @Nested
    class getFollowListTest {

        @Test
        @DisplayName("팔로잉 목록 조회 성공 테스트")
        void getFollowingListSuccessTest() {

            Follow testFollow = TestMockData.testFollow();
            List<Follow> follows = new ArrayList();
            follows.add(testFollow);

            User testUser = TestMockData.testUser();
            List<User> users = new ArrayList();
            users.add(testUser);

            List<FollowingResponseDto> resultList = followServiceImpl.getFollowingList(
                testFromUserId);

            for (int i = 0; i < resultList.size(); i++) {
                assertThat(resultList.get(i).getToUserId()).isEqualTo(testUser.getId());
                assertThat(resultList.get(i).getUsername()).isEqualTo(users.get(i).getUsername());
            }
        }

        @Test
        @DisplayName("팔로워 목록 조회 성공 테스트")
        void getFollowerListSuccessTest() {
            Follow testFollow = TestMockData.testFollow();
            List<Follow> follows = new ArrayList();
            follows.add(testFollow);

            User testUser = TestMockData.testUser();
            List<User> users = new ArrayList();
            users.add(testUser);

            List<FollowerResponseDto> resultList = followServiceImpl.getFollowerList(testToUserId);

            for (int i = 0; i < resultList.size(); i++) {
                assertThat(resultList.get(i).getFromUserId()).isEqualTo(testUser.getId());
                assertThat(resultList.get(i).getUsername()).isEqualTo(users.get(i).getUsername());
            }
        }
    }

    @Nested
    class deleteFollowTest {

        @Test
        @DisplayName("삭제할 팔로우가 없는 경우 실패 테스트")
        void followNotFoundTest() {

            given(followRepository.findByFromUserIdAndToUserId(anyLong(), anyLong())).willReturn(
                Optional.empty());

            assertThrows(InvalidInputException.class, () -> {
                followServiceImpl.deleteFollow(testFromUserId, testToUserId);
            });
        }

        @Test
        @DisplayName("팔로우 삭제 성공 테스트")
        void deleteFollowSuccessTest() {

            Follow testFollow = TestMockData.testFollow();

            given(followRepository.findByFromUserIdAndToUserId(anyLong(), anyLong())).willReturn(
                Optional.ofNullable(testFollow));

            followServiceImpl.deleteFollow(testFromUserId, testToUserId);

            then(followRepository).should(times(1)).delete(any(Follow.class));
        }
    }
}
