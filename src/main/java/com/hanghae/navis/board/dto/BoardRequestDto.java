package com.hanghae.navis.board.dto;

import com.hanghae.navis.common.dto.HashtagRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
//@Setter
@NoArgsConstructor
public class BoardRequestDto {
    private String title;
    private String subtitle;
    private String content;
    private Long important;
    private String hashtagList;
    List<MultipartFile> multipartFiles;

    public BoardRequestDto(String title, String content, String subtitle, String important, String hashtagList, List<MultipartFile> multipartFiles) {
        this.title = title;
        this.content = content;
        this.subtitle = subtitle;
        this.hashtagList = hashtagList;
        this.important = Long.parseLong(important);
        this.multipartFiles = multipartFiles;
    }
}
