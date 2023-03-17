package com.hanghae.navis.user.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Setter
@Getter
public class ProfileUpdateRequestDto {
    private MultipartFile profileImage;
    private String nickname;
}
