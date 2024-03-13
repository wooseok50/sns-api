package com.sns.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

import com.sns.data.TestMockData;
import com.sns.domain.post.dto.PostRequestDto;
import com.sns.domain.post.dto.PostResponseDto;
import com.sns.domain.post.entity.Post;
import com.sns.domain.post.repository.PostRepository;
import com.sns.domain.post.service.PostServiceImpl;
import com.sns.global.exception.InvalidInputException;
import com.sns.global.exception.PostNotFoundException;
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
class PostServiceImplTest {
    /*
     * 게시글 생성 메서드의 예외 로직이 없음으로 테스트 생략
     * 게시글 수정, 삭제 메서드의 예외 로직이 같은 이유로 필요한 메서드만 테스트
     * 게시글 전체 조회 로직이 repository 단의 로직으로 생략
     * */
    @InjectMocks
    private PostServiceImpl postServiceImpl;

    @Mock
    private PostRepository postRepository;

    Long testPostId;
    Long testUserId;

    @BeforeEach
    void setUp() {
        testPostId = 1L;
        testUserId = 2L;
    }

    @Nested
    public class getPost {

        @Test
        @DisplayName("조회할 게시글이 존재하지 않을 경우, 조회 실패 테스트")
        void getPostNotFoundTest() {
            given(postRepository.searchPost(testPostId)).willReturn(Optional.empty());

            assertThrows(PostNotFoundException.class, () -> {
                postServiceImpl.getPost(testPostId);
            });
        }

        @Test
        @DisplayName("해당 게시글 조회 성공 테스트")
        void getPostSuccessTest() {
            Post testPost = TestMockData.testPost();
            given(postRepository.searchPost(testPostId)).willReturn(Optional.of(testPost));

            PostResponseDto result = postServiceImpl.getPost(testPostId);

            assertThat(result.getTitle()).isEqualTo(testPost.getTitle());
            assertThat(result.getContent()).isEqualTo(testPost.getContent());
        }
    }

    @Nested
    public class updatePost {

        @Test
        @DisplayName("해당 게시글의 유저가 아닌 경우, 게시글 수정 실패 테스트")
        void updatePostFailureWhenNotOwner() {

            Long defaultUserId = 123L;
            Post testPost = TestMockData.testPost();
            PostRequestDto testPostRequestDto = new PostRequestDto("testUsername", "title",
                "content");

            given(postRepository.findById(anyLong())).willReturn(Optional.of(testPost));

            assertThrows(InvalidInputException.class, () -> {
                postServiceImpl.updatePost(testPostId, defaultUserId, testPostRequestDto);
            });
        }

        @Test
        @DisplayName("게시글 수정 성공 테스트")
        void updatePostSuccessTest() {
            Post testPost = TestMockData.testPost();
            PostRequestDto testPostRequestDto = new PostRequestDto("testUsername", "title",
                "content");
            given(postRepository.findById(anyLong())).willReturn(Optional.of(testPost));

            postServiceImpl.updatePost(testPostId, testUserId, testPostRequestDto);

            // 해당 메서드의 반환 값이 없기 때문에 아래 방식으로 검증.
            assertThat(testPost.getTitle()).isEqualTo("title");
        }
    }

    @Nested
    public class findPost {

        @Test
        @DisplayName("postId의 게시글이 존재하지 않을 경우, 실패 테스트")
        void findPostNotFoundTest() {
            given(postRepository.findById(anyLong())).willReturn(Optional.empty());

            assertThrows(PostNotFoundException.class, () -> {
                postServiceImpl.findPost(testPostId);
            });
        }
    }
}
