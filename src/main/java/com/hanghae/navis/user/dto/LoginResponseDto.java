package com.hanghae.navis.user.dto;

import com.hanghae.navis.user.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LoginResponseDto<T> {

    private String username;
    private String nickname;
    private T token;

    public LoginResponseDto(User user, T token) {
        this.username = user.getUsername();
        this.nickname = user.getNickname();
        this.token = token;
    }
}
