package com.sns.domain.follow.controller;

import com.sns.domain.follow.dto.FollowerResponseDto;
import com.sns.domain.follow.dto.FollowingResponseDto;
import com.sns.domain.follow.service.FollowService;
import com.sns.global.security.UserDetailsImpl;
import com.sns.domain.notification.service.NotificationService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class FollowController {

    private final FollowService followService;
    private final NotificationService notificationService;

    @PostMapping("/follows/{toUserId}")
    public ResponseEntity<Void> createFollow(
        @AuthenticationPrincipal UserDetailsImpl userDetails,
        @PathVariable Long toUserId
    ) {
        followService.createFollow(userDetails.getUser().getId(), toUserId);
        notificationService.notifyFollow(toUserId);
        return ResponseEntity.status(HttpStatus.CREATED.value()).build();
    }

    @GetMapping("/users/{userId}/follows/following")
    public List<FollowingResponseDto> getFollowingList(
        @PathVariable Long userId
    ) {
        List<FollowingResponseDto> followingResponseDtos = followService.getFollowingList(userId);
        return ResponseEntity.ok().body(followingResponseDtos).getBody();
    }

    @GetMapping("/users/{userId}/follows/follower")
    public List<FollowerResponseDto> getFollowerList(
        @PathVariable Long userId
    ) {
        List<FollowerResponseDto> followerResponseDtos = followService.getFollowerList(userId);
        return ResponseEntity.ok().body(followerResponseDtos).getBody();
    }

    @DeleteMapping("/follows/{toUserId}")
    public ResponseEntity<Void> deleteFollow(
        @AuthenticationPrincipal UserDetailsImpl userDetails,
        @PathVariable Long toUserId
    ) {
        followService.deleteFollow(userDetails.getUser().getId(), toUserId);
        return ResponseEntity.ok().build();
    }
}
