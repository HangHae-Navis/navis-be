package com.hanghae.navis.common.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor
public class CommentRequestDto {
    @Size(min = 1, max = 300)
    private String content;

    public CommentRequestDto(String content) {
        this.content = content;
    }
}
