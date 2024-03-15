package com.sns.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.sns.configuration.JPAConfiguration;
import com.sns.domain.like.entity.Like;
import com.sns.domain.like.repository.LikeRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.util.ReflectionTestUtils;

@Import(JPAConfiguration.class)
@DataJpaTest
class LikeRepositoryTest {

    @Autowired
    LikeRepository likeRepository;

    Long testPostId;
    Long testUserId;

    Like testLike;

    @BeforeEach
    void setUp() {
        testPostId = 1L;
        testUserId = 2L;

        testLike = new Like(testPostId, testUserId);
        ReflectionTestUtils.setField(testLike, "id", 3L);
    }

    @Test
    @DisplayName("postId와 userId로 생성된 Like 찾기 성공 테스트")
    void likeFoundTest() {

        likeRepository.save(testLike);

        Optional<Like> result = likeRepository.findLikeByPostIdAndUserId(testPostId, testUserId);

        // then
        assertEquals(result.get().getPostId(), testLike.getPostId());
        assertEquals(result.get().getUserId(), testLike.getUserId());
    }
}
