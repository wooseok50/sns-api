package com.sns.post.repository;

import com.sns.post.entity.Post;
import java.util.Optional;

public interface SearchPost {
    Optional<Post> searchPost(Long postId);
}
