package com.hanghae.navis.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Setter
@Getter
public class ProfileUpdateRequestDto {
    private MultipartFile profileImage;
    @NotNull
    private String nickname;
    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,15}", message = "비밀번호는 영문 대,소문자와 숫자, 특수기호가 적어도 1개 이상씩 포함된 8자 ~ 15자의 비밀번호여야 합니다.")
    @Schema(example = "Dkssud1!")
    private String password;






}
