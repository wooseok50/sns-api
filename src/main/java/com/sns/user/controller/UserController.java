package com.sns.user.controller;

import com.sns.global.dto.CommonResponseDto;
import com.sns.global.security.UserDetailsImpl;
import com.sns.user.dto.SignupRequestDto;
import com.sns.user.dto.UserResponseDto;
import com.sns.user.service.UserService;
import java.net.MalformedURLException;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.Parameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.parameters.P;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
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
    public ResponseEntity<CommonResponseDto<String>> logout(
        @AuthenticationPrincipal UserDetails userDetails
    ) {
        userService.logout(userDetails);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponseDto> getUserInfo(
        @PathVariable("userId") Long userId
    ) {
        UserResponseDto responseDto = userService.getUserInfo(userId);
        return ResponseEntity.status(HttpStatus.OK.value()).body(responseDto);
    }
}