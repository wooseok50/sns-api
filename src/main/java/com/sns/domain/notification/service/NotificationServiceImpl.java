package com.sns.domain.notification.service;

import com.sns.domain.follow.entity.Follow;
import com.sns.domain.follow.service.FollowService;
import com.sns.domain.like.entity.Like;
import com.sns.domain.like.service.LikeService;
import com.sns.domain.notification.controller.NotificationController;
import com.sns.domain.notification.entity.Notification;
import com.sns.domain.notification.repository.NotificationRepository;
import com.sns.domain.post.service.PostService;
import com.sns.domain.user.service.UserService;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserService userService;
    private final PostService postService;
    private final LikeService likeService;
    private final FollowService followService;

    private static Map<Long, Integer> notificationCounts = new HashMap<>();

    @Override
    public SseEmitter subscribe(Long userId) { // userId : 알림 받을 계정
        // 현재 클라이언트를 위한 sseEmitter 생성
        SseEmitter sseEmitter = new SseEmitter(Long.MAX_VALUE);
        try {
            // 연결
            sseEmitter.send(SseEmitter.event().name("connect"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // user 의 pk 값을 key 값으로 해서 sseEmitter 를 저장
        NotificationController.sseEmitters.put(userId, sseEmitter);

        // 4. 연결 종료 처리, 더이상 필요하지 않은 연결 정보를 정리
        sseEmitter.onCompletion(() -> NotificationController.sseEmitters.remove(userId));
        sseEmitter.onTimeout(() -> NotificationController.sseEmitters.remove(userId));
        sseEmitter.onError((e) -> NotificationController.sseEmitters.remove(userId));

        return sseEmitter;
    }

    @Override
    public void notifyLike(Long postId) {
        Like receiveLike = likeService.findLatestLike(postId);
        long userId = postService.findPost(postId).getUserId();

        if (NotificationController.sseEmitters.containsKey(userId)) {
            SseEmitter sseEmitter = NotificationController.sseEmitters.get(userId);
            try {
                Map<String, String> eventData = new HashMap<>();
                eventData.put("contents",
                    userService.findUser(receiveLike.getUserId()).getUsername()
                        + "님이 회원님의 게시물을 좋아합니다.❤️");

                sseEmitter.send(SseEmitter.event().name("addLike").data(eventData));

                // DB 저장
                Notification notification = new Notification();
                notification.setPostId(postId);
                notification.setContents("좋아요");
                notification.setSender(userService.findUser(receiveLike.getUserId()).getUsername());
                notificationRepository.save(notification);

                // 알림 개수 증가
                notificationCounts.put(userId, notificationCounts.getOrDefault(userId, 0) + 1);

                // 현재 알림 개수 전송
                sseEmitter.send(SseEmitter.event().name("notificationCount")
                    .data(notificationCounts.get(userId)));

            } catch (IOException e) {
                NotificationController.sseEmitters.remove(userId);
            }
        }
    }

    @Override
    public void notifyFollow(Long toUserId) {
        long userId = userService.findUser(toUserId).getId();
        Follow follow = followService.findLatestUser(toUserId);

        if (NotificationController.sseEmitters.containsKey(userId)) {
            SseEmitter sseEmitter = NotificationController.sseEmitters.get(userId);
            try {
                Map<String, String> eventData = new HashMap<>();
                eventData.put("sender", userService.findUser(follow.getFromUserId()).getUsername());
                eventData.put("contents", userService.findUser(follow.getFromUserId()).getUsername()
                    + "님이 팔로우 요청을 보냈습니다.");

                sseEmitter.send(SseEmitter.event().name("addFollow").data(eventData));

                // DB 저장
                Notification notification = new Notification();
                notification.setUserId(userService.findUser(userId).getId());
                notification.setContents("팔로우 요청");
                notification.setSender(userService.findUser(follow.getFromUserId()).getUsername());
                notificationRepository.save(notification);

                // 알림 개수 증가
                notificationCounts.put(userId, notificationCounts.getOrDefault(userId, 0) + 1);

                // 현재 알림 개수 전송
                sseEmitter.send(SseEmitter.event().name("notificationCount")
                    .data(notificationCounts.get(userId)));

            } catch (IOException e) {
                NotificationController.sseEmitters.remove(userId);
            }
        }
    }

    @Override
    public void deleteNotification(Long id) throws IOException {
        Notification notification = notificationRepository.findById(id).orElseThrow(
            () -> new IllegalArgumentException("알림을 찾을 수 없습니다.")
        );

        Long userId = 0L; // 알림 받는 유저의 유저아이디.

        if (notification.getPostId() != null) { // 게시글의 좋아요, 댓글 알림일 경우
            userId = postService.findPost(notification.getPostId()).getUserId();
        } else { // 팔로우 일경우
            userId = notification.getUserId();
        }

        notificationRepository.delete(notification);

        // 알림 개수 감소
        if (notificationCounts.containsKey(userId)) {
            int currentCount = notificationCounts.get(userId);
            if (currentCount > 0) {
                notificationCounts.put(userId, currentCount - 1);
            }
        }

        // 현재 알림 개수 전송
        SseEmitter sseEmitter = NotificationController.sseEmitters.get(userId);
        sseEmitter.send(
            SseEmitter.event().name("notificationCount").data(notificationCounts.get(userId)));
    }
}
