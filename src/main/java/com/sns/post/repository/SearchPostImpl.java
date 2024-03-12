package com.sns.post.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sns.post.entity.Post;
import com.sns.post.entity.QPost;
import java.util.Optional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SearchPostImpl implements SearchPost {

    private final JPAQueryFactory jpaQueryFactory;
    QPost post = QPost.post;

    @Override
    public Optional<Post> searchPost(Long postId) {
        return Optional.ofNullable(jpaQueryFactory.selectFrom(post)
            .where(post.id.eq(postId)
                .and(post.deleted_YN.eq("N")))
            .fetchOne());
    }
}
