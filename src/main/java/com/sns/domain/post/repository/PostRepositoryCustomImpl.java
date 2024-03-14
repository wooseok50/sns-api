package com.sns.domain.post.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sns.domain.post.entity.Post;
import com.sns.domain.post.entity.QPost;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@RequiredArgsConstructor
public class PostRepositoryCustomImpl implements PostRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;
    QPost post = QPost.post;

    @Override
    public Optional<Post> searchPost(Long postId) {
        return Optional.ofNullable(jpaQueryFactory.selectFrom(post)
            .where(post.id.eq(postId)
                .and(post.deleted_YN.eq("N")))
            .fetchOne());
    }

    @Override
    public Page<Post> queryPosts(String title, String username, Pageable pageable) {

        BooleanBuilder predicate = new BooleanBuilder();
        predicate.and(post.deleted_YN.eq("N"));

        if (title != null) {
            predicate.and(post.title.contains(title));
        }
        if (username != null) {
            predicate.and(post.username.eq(username));
        }

        List<Post> posts = jpaQueryFactory
            .selectFrom(post)
            .where(predicate)
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        long totalCount = jpaQueryFactory
            .selectFrom(post)
            .where(predicate)
            .fetchCount();

        return new PageImpl<>(posts, pageable, totalCount);
    }
}
