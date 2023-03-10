package com.hanghae.navis.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LoginResponseDto<T> {
    private String nickname;
    private T token;

    public LoginResponseDto(String nickname, T token) {
        this.nickname = nickname;
        this.token = token;
    }


}
