package com.sns.domain.like.controller;

import com.sns.global.security.UserDetailsImpl;
import com.sns.domain.like.service.LikeService;
import com.sns.domain.like.dto.LikeResponseDto;
import com.sns.domain.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts/{postId}/like")
public class LikeController {

    private final LikeService likeService;
    private final NotificationService notificationService;

    @PostMapping
    public ResponseEntity<Void> createPostLike(@PathVariable Long postId,
        @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        likeService.createPostLike(postId, userDetails.getUser().getId());
        notificationService.notifyLike(postId);
        return ResponseEntity.status(HttpStatus.CREATED.value()).build();
    }

    @GetMapping
    public ResponseEntity<LikeResponseDto> countLikes(@PathVariable Long postId
    ) {
        LikeResponseDto responseDto = likeService.countLikes(postId);
        return ResponseEntity.ok().body(responseDto);
    }

    @DeleteMapping
    public ResponseEntity<Void> deletePostLike(@PathVariable Long postId,
        @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        likeService.deletePostLike(postId, userDetails.getUser().getId());
        return ResponseEntity.ok().build();
    }
}

