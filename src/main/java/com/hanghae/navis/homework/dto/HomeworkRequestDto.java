package com.hanghae.navis.homework.dto;

import com.hanghae.navis.board.dto.BoardRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class HomeworkRequestDto extends BoardRequestDto {
    private Long expirationDate;

    public HomeworkRequestDto(String title, String subtitle, String content, String important, String hashtagList, Long expirationDate, List<MultipartFile> multipartFiles) {
        super(title, subtitle, content, important, hashtagList, multipartFiles);
        this.expirationDate = expirationDate;
    }
}
