package com.sns.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

import com.sns.data.TestMockData;
import com.sns.domain.like.dto.LikeResponseDto;
import com.sns.domain.like.entity.Like;
import com.sns.domain.like.repository.LikeRepository;
import com.sns.domain.like.service.LikeServiceImpl;
import com.sns.domain.post.service.PostServiceImpl;
import com.sns.global.exception.InvalidInputException;
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
class LikeServiceImplTest {

    @InjectMocks
    private LikeServiceImpl likeServiceImpl;
    @Mock
    private LikeRepository likeRepository;
    @Mock
    private PostServiceImpl postServiceImpl;

    Long testPostId;
    Long testUserId;

    @BeforeEach
    void setUp() {
        testPostId = 1L;
        testUserId = 2L;
    }

    @Nested
    public class createPostLike {

        @Test
        @DisplayName("이미 해당 개시글에 좋아요를 눌렀을 경우 좋아요 생성 실패 테스트")
        void alreadyLikedTest() {

            Like testLike = TestMockData.testLike();
            given(likeRepository.findLikeByPostIdAndUserId(anyLong(), anyLong())).willReturn(
                Optional.of(testLike));

            assertThrows(InvalidInputException.class, () -> {
                likeServiceImpl.createPostLike(testPostId, testUserId);
            });
        }

        @Test
        @DisplayName("좋아요 생성 성공 테스트")
        void createPostLikeSuccessTest() {

            likeServiceImpl.createPostLike(testPostId, testUserId);

            then(likeRepository).should(times(1)).save(any(Like.class));
        }
    }

    @Nested
    public class getPostLike {

        @Test
        @DisplayName("좋아요 수 조회 성공 테스트")
        void getPostLikeSuccessTest() {

            Long testLikeCount = 1L;
            given(likeRepository.countByPostId(testPostId)).willReturn(Optional.of(testLikeCount));

            LikeResponseDto result = likeServiceImpl.countLikes(testPostId);

            assertThat(result.getLikeCount()).isEqualTo(testLikeCount);
        }
    }

    @Nested
    public class deletePostLike {

        @Test
        @DisplayName("이미 해당 게시글에 좋아요 삭제한 경우, 좋아요 삭제 실패 테스트")
        void alreadyDeleteLikedTest() {

            given(likeRepository.findLikeByPostIdAndUserId(anyLong(), anyLong())).willReturn(
                Optional.empty());

            assertThrows(InvalidInputException.class, () -> {
                likeServiceImpl.deletePostLike(testPostId, testUserId);
            });
        }

        @Test
        @DisplayName("게시글 좋아요 삭제 성공 테스트")
        void deletePostLikeSuccessTest() {

            Like testLike = TestMockData.testLike();
            given(likeRepository.findLikeByPostIdAndUserId(anyLong(), anyLong())).willReturn(
                Optional.of(testLike));

            likeServiceImpl.deletePostLike(testPostId, testUserId);

            then(likeRepository).should(times(1)).delete(any(Like.class));
        }
    }
}
