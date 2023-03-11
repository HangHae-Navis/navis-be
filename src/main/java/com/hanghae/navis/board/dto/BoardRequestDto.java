package com.hanghae.navis.board.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Getter
@NoArgsConstructor
public class BoardRequestDto {
    private String content;
    private MultipartFile fileUrl;

    public BoardRequestDto (String content, MultipartFile fileUrl) {
        this.content = content;
        this.fileUrl = fileUrl;
    }
}
