package com.sns.like.repository;

import com.sns.like.entity.Like;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like, Long> {

    Optional<Like> findLikeByPostIdAndUserId(Long postId, Long userId);

    Optional<Long> countByPostId(Long postId);

    Optional<Like> findFirstByPostIdOrderByCreatedAtDesc(Long postId);
}
