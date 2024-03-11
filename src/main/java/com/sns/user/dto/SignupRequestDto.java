package com.sns.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SignupRequestDto {

    @NotBlank
    @Email(message = "Please enter a valid email")
    @Size(max = 40)
    private String email;

    @NotBlank
    @Size(max = 20)
    private String username;

    @NotBlank(message = "비밀번호를 입력하세요.")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,20}$", message = "비밀번호는 8글자~20자, 대문자 1개, 소문자 1개, 숫자 1개, 특수문자 1개 이상 포함하세요.")
    private String password;
}
