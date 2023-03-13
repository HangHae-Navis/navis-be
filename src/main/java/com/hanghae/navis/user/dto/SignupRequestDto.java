package com.hanghae.navis.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.*;

@Setter
@Getter
public class SignupRequestDto {

    @Pattern(regexp = "^[a-z0-9]+$", message = "아이디는 소문자랑 숫자만 가능합니다.")
    @Size(min=4, max=10, message = "4 ~ 10길이의 소문자, 숫자만 가능합니다.")

    @Email
    @Schema(example = "user@gmail.com")
    private String username;

    @NotNull(message = "닉네임은 공백일 수 없습니다.")
    @Min(value = 1, message = "1글자 이상이어야 합니다.")
    @Schema(example = "테스트")
    private String nickname;

    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,15}", message = "비밀번호는 영문 대,소문자와 숫자, 특수기호가 적어도 1개 이상씩 포함된 8자 ~ 15자의 비밀번호여야 합니다.")
    @Schema(example = "Dkssud1!")
    private String password;
}

