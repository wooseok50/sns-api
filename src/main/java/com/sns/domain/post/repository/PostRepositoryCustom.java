package com.sns.domain.post.repository;

import com.sns.domain.post.entity.Post;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostRepositoryCustom {
    Optional<Post> searchPost(Long postId);

    Page<Post> findBySearchOption(String title, String username, Pageable pageable);
}
