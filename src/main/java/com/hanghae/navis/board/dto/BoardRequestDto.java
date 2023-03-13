package com.hanghae.navis.board.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
public class BoardRequestDto {
    private String content;

//    private MultipartFile fileUrl;

//    public BoardRequestDto (String content, MultipartFile fileUrl) {
//        this.content = content;
//        this.fileUrl = fileUrl;
//    }

    public BoardRequestDto(String content) {
        this.content = content;
    }
}
