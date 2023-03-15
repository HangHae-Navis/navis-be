package com.hanghae.navis.board.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
public class BoardFileRequestDto {
    private MultipartFile fileUrl;

    public BoardFileRequestDto (MultipartFile fileUrl) {
        this.fileUrl = fileUrl;
    }
}
