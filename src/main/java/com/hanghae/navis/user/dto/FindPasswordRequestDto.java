package com.hanghae.navis.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Setter
@Getter
public class FindPasswordRequestDto {
    @Email
    @Schema(example = "user@gmail.com")
    private String username;

    @NotNull
    private String key;
}
