package com.hanghae.navis.common.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class FileRequestDto {
    private MultipartFile fileUrl;

    public FileRequestDto(MultipartFile fileUrl) {
        this.fileUrl = fileUrl;
    }
}
