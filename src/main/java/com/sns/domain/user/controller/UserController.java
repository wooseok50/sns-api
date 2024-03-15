package com.sns.domain.user.controller;

import com.sns.global.dto.CommonResponseDto;
import com.sns.global.security.UserDetailsImpl;
import com.sns.domain.user.dto.SignupRequestDto;
import com.sns.domain.user.dto.UserResponseDto;
import com.sns.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<Void> signup(
        @Validated @RequestBody SignupRequestDto requestDto
    ) {
        userService.signup(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/logout")
    public ResponseEntity<CommonResponseDto<Void>> logout(
        @AuthenticationPrincipal UserDetailsImpl userDetailsImpl
    ) {
        userService.logout(userDetailsImpl.getUser().getId());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponseDto> getUserInfo(
        @PathVariable("userId") Long userId
    ) {
        UserResponseDto responseDto = userService.getUserInfo(userId);
        return ResponseEntity.ok().body(responseDto);
    }

    @GetMapping
    public ResponseEntity<Page<UserResponseDto>> getAllUserInfo(
        @RequestParam("page") int page,
        @RequestParam("size") int size
    ) {
        Page<UserResponseDto> responseDto = userService.getAllUserInfo(page - 1, size);
        return ResponseEntity.ok().body(responseDto);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteUserInfo(
        @AuthenticationPrincipal UserDetailsImpl userDetailsImpl
    ) {
        userService.deleteUserInfo(userDetailsImpl.getUser());
        return ResponseEntity.ok().build();
    }
}
