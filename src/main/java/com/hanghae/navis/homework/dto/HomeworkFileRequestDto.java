package com.hanghae.navis.homework.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class HomeworkFileRequestDto {
    private List<MultipartFile> multipartFiles;

    public HomeworkFileRequestDto(List<MultipartFile> multipartFiles) {
        this.multipartFiles = multipartFiles;
    }
}
