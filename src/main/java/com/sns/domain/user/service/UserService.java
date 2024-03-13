package com.sns.domain.user.service;

import com.sns.domain.user.dto.SignupRequestDto;
import com.sns.domain.user.dto.UserResponseDto;
import com.sns.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

public interface UserService {

    /**
     * 회원가입
     * 유저 생성
     *
     * @param requestDto 회원가입 정보
     */
    @Transactional
    void signup(SignupRequestDto requestDto);

    /**
     * 로그아웃
     * refreshToken 삭제
     *
     * @param userId 로그아웃 유저 ID
     */
    @Transactional
    void logout(Long userId);

    /**
     * 해당 유저 정보 조회
     *
     * @param userId 조회할 유저 ID
     * @return 삭제된(soft delete) 유저 정보를 제외한 정보
     */
    @Transactional(readOnly = true)
    UserResponseDto getUserInfo(Long userId);

    /**
     * 전체 유저 정보 조회 (Page)
     *
     * @param page 조회 page
     * @param size 조회 size
     * @return 삭제된(soft delete) 전체 유저 정보를 제외한 정보
     */
    @Transactional(readOnly = true)
    Page<UserResponseDto> getAllUserInfo(int page, int size);

    /**
     * 유저 정보 삭제 (Soft delete)
     * 유저의 deleted_YN 필드의 값을 "Y"로 할당
     *
     * @param user 유저 정보 삭제 요청자
     */
    @Transactional
    void deleteUserInfo(User user);

    /**
     * 유저 존재 여부 체크
     * 유저가 존재하지 않을 경우 exception
     *
     * @param userId 조회할 유저 ID
     */
    void checkValidateUser(Long userId);

    /**
     * 유저 객체 반환
     * 유저가 존재하지 않을 경우 exception
     *
     * @param userId 조회할 유저 ID
     * @return 유저 객체
     */
    User findUser(Long userId);
}
