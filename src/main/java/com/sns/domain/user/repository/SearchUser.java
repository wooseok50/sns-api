package com.sns.domain.user.repository;

import com.sns.domain.user.entity.User;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SearchUser {

    Optional<User> searchUserInfo(Long userId);

    Page<User> searchAllUserInfo(Pageable pageable);
}
