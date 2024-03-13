package com.sns.follow.controller;

import com.sns.follow.dto.FollowerResponseDto;
import com.sns.follow.dto.FollowingResponseDto;
import com.sns.follow.service.FollowService;
import com.sns.global.security.UserDetailsImpl;
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

    @PostMapping("/follows/{toUserId}")
    public ResponseEntity<Void> createFollow(
        @AuthenticationPrincipal UserDetailsImpl userDetails,
        @PathVariable Long toUserId
    ) {
        followService.createFollow(userDetails.getUser().getId(), toUserId);
        return ResponseEntity.status(HttpStatus.OK.value()).build();
    }

    @GetMapping("/users/{userId}/follows/following")
    public List<FollowingResponseDto> getFollowingList(
        @PathVariable Long userId
    ) {
        List<FollowingResponseDto> followingResponseDtos = followService.getFollowingList(userId);
        return ResponseEntity.status(HttpStatus.OK.value()).body(followingResponseDtos).getBody();
    }

    @GetMapping("/users/{userId}/follows/follower")
    public List<FollowerResponseDto> getFollowerList(
        @PathVariable Long userId
    ) {
        List<FollowerResponseDto> followerResponseDtos = followService.getFollowerList(userId);
        return ResponseEntity.status(HttpStatus.OK.value()).body(followerResponseDtos).getBody();
    }

    @DeleteMapping("/follows/{toUserId}")
    public ResponseEntity<Void> deleteFollow(
        @AuthenticationPrincipal UserDetailsImpl userDetails,
        @PathVariable Long toUserId
    ) {
        followService.deleteFollow(userDetails.getUser().getId(), toUserId);
        return ResponseEntity.status(HttpStatus.OK.value()).build();
    }
}
