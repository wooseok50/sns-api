package com.sns.domain.follow.repository;

import com.sns.domain.follow.entity.Follow;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Long>, FollowRepositoryCustom {

    Optional<Follow> findByFromUserIdAndToUserId(Long fromUserId, Long toUserId);

    Optional<Follow> findFirstByToUserIdOrderByCreatedAtDesc(Long toUserId);
}
