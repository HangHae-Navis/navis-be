package com.hanghae.navis.board.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class BoardRequestDto {
    @Size(min = 1, max = 50)
    private String title;

    @Size(max = 50)
    private String subtitle;

    @Size(max = 3000)
    private String content;

    private Long important;

    private String hashtagList;

    private List<MultipartFile> multipartFiles;

    public BoardRequestDto(String title, String content, String subtitle, String important, String hashtagList, List<MultipartFile> multipartFiles) {
        this.title = title;
        this.content = content;
        this.subtitle = subtitle;
        this.hashtagList = hashtagList;
        this.important = Long.parseLong(important);
        this.multipartFiles = multipartFiles;
    }

    public BoardRequestDto(String title, String hashtagList) {
        this.title = title;
        this.hashtagList = hashtagList;
    }
}
