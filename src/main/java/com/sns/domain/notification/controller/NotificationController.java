package com.sns.domain.notification.controller;

import com.sns.domain.notification.service.NotificationService;
import com.sns.global.security.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Tag(name = "Notification", description = "실시간 알림")
@RestController
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;
    public static Map<Long, SseEmitter> sseEmitters = new ConcurrentHashMap<>();

    @Operation(summary = "알림 생성", description = "실시간 알림 서비스 연결")
    @GetMapping("/notification/subscribe")
    public SseEmitter subscribe(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        SseEmitter sseEmitter = notificationService.subscribe(userDetails.getUser().getId());

        return sseEmitter;
    }

    @Operation(summary = "알림 삭제")
    @DeleteMapping("/notification/delete/{id}")
    public ResponseEntity<Void> deleteNotification(@PathVariable Long id) throws IOException {
        notificationService.deleteNotification(id);
        return ResponseEntity.ok().build();
    }
}
