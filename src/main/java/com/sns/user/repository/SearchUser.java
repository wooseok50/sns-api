package com.sns.user.repository;

import com.sns.user.entity.User;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SearchUser {

    Optional<User> searchUserInfo(Long userId);

    Page<User> searchAllUserInfo(Pageable pageable);
}
