package com.sns.domain.notification.repository;

import com.sns.domain.notification.entity.Notification;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    Optional<Notification> findById(Long id);
}

