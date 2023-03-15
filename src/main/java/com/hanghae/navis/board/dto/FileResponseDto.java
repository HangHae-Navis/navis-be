package com.hanghae.navis.board.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FileResponseDto {

    private String fileTitle;
    private String fileUrl;

    public FileResponseDto(String fileTitle, String fileUrl) {
        this.fileTitle = fileTitle;
        this.fileUrl = fileUrl;
    }
}


