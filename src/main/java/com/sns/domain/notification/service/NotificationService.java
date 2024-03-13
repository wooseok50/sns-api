package com.sns.domain.notification.service;

import java.io.IOException;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface NotificationService {

    /**
     * 주어진 사용자 ID에 대한 신규 SseEmitter를 생성하여 구독
     *
     * @param userId 구독할 사용자 ID
     * @return 생성된 SseEmitter 객체
     */
    SseEmitter subscribe(Long userId);

    /**
     * 주어진 게시물 ID에 대한 좋아요 알림을 전송
     *
     * @param postId 좋아요 알림을 전송할 게시물 ID
     */
    void notifyLike(Long postId);

    /**
     * 주어진 대상 사용자 ID에 대한 팔로우 알림을 전송
     *
     * @param toUserId 팔로우 알림을 전송할 대상 사용자 ID
     */
    void notifyFollow(Long toUserId);

    /**
     * 주어진 알림 ID에 해당하는 알림을 삭제
     *
     * @param id 삭제할 알림 ID
     * @throws IOException 입출력 오류가 발생한 경우 exception
     */
    void deleteNotification(Long id) throws IOException;
}
