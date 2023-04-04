package com.hanghae.navis.board.dto;

import com.hanghae.navis.common.dto.HashtagRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.Multipart;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class BoardRequestDto {
    @Size(min = 1, max = 50)
    private String title;

    private String subtitle;

    @Size(min = 1, max = 3000)
    private String content;

    @Size(min = 1, max = 50)
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
}
