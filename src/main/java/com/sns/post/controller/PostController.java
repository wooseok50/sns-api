package com.sns.post.controller;

import com.sns.global.security.UserDetailsImpl;
import com.sns.post.dto.PostRequestDto;
import com.sns.post.dto.PostResponseDto;
import com.sns.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    @PostMapping
    public ResponseEntity<Void> createPost(
        @RequestBody PostRequestDto postRequestDto,
        @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        postService.createPost(postRequestDto, userDetails.getUser());
        return ResponseEntity.status(HttpStatus.CREATED.value()).build();
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostResponseDto> getPost(
        @PathVariable Long postId
    ) {
        PostResponseDto responseDto = postService.getPost(postId);
        return ResponseEntity.status(HttpStatus.OK.value()).body(responseDto);
    }

//    @PutMapping("/{postId}")
//    public ResponseEntity<Void> updatePost(
//        @PathVariable Long postId,
//        @AuthenticationPrincipal UserDetailsImpl userDetails
//    ) {
//        postService.updatePost(postId, userDetails.getUser());
//        return ResponseEntity.status(HttpStatus.OK.value()).build();
//    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(
        @PathVariable Long postId,
        @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        postService.deletePost(postId, userDetails.getUser().getId());
        return ResponseEntity.status(HttpStatus.OK.value()).build();
    }
}
