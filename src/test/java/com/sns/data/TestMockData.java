package com.sns.data;

import com.sns.domain.follow.entity.Follow;
import com.sns.domain.like.entity.Like;
import com.sns.domain.post.entity.Post;
import com.sns.domain.user.entity.User;
import com.sns.domain.user.entity.UserRole;

public class TestMockData {

    public static User testUser() {
        return new User(1L, "test@email.com", "testUsername", "testPassword", "N", UserRole.USER);
    }

    public static Post testPost() {
        return Post.builder()
            .userId(2L)
            .username("testUsername")
            .title("testTitle")
            .content("testContent")
            .deleted_YN("N")
            .build();
    }

    public static Follow testFollow() {
        Long testFromUserId = 1L;
        Long testToUserId = 2L;

        return Follow.builder()
            .fromUserId(testFromUserId)
            .toUserId(testToUserId)
            .build();
    }

    public static Like testLike() {
        Long testPostId = 1L;
        Long testUserId = 2L;

        return new Like(testPostId, testUserId);
    }
}
