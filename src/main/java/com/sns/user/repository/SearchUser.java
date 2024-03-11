package com.sns.user.repository;

import com.sns.user.entity.User;
import java.util.Optional;

public interface SearchUser {

    Optional<User> searchUserInfo(Long userId);
}
