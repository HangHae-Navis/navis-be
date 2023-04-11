package com.hanghae.navis.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class KakaoUserInfoDto {
    private Long id;
    private String email;
    private String nickname;
    private String token;

    public KakaoUserInfoDto(Long id, String nickname, String email, String token) {
        this.id = id;
        this.nickname = nickname;
        this.email = email;
        this.token = token;
    }
}