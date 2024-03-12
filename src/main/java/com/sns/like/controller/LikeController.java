package com.sns.like.controller;

import com.sns.global.security.UserDetailsImpl;
import com.sns.like.dto.LikeResponseDto;
import com.sns.like.service.LikeService;
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

    @PostMapping
    public ResponseEntity<Void> createPostLike(@PathVariable Long postId,
        @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        likeService.createPostLike(postId, userDetails.getUser().getId());
        return ResponseEntity.status(HttpStatus.CREATED.value()).build();
    }

    @GetMapping
    public ResponseEntity<LikeResponseDto> countLikes(@PathVariable Long postId
    ) {
        LikeResponseDto responseDto = likeService.countLikes(postId);
        return ResponseEntity.status(HttpStatus.OK.value()).body(responseDto);
    }

    @DeleteMapping
    public ResponseEntity<Void> deletePostLike(@PathVariable Long postId,
        @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        likeService.deletePostLike(postId, userDetails.getUser().getId());
        return ResponseEntity.status(HttpStatus.OK.value()).build();
    }
}

