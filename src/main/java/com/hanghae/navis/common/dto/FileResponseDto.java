package com.hanghae.navis.common.dto;

import com.hanghae.navis.common.entity.File;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@Builder
public class FileResponseDto {

    private String fileTitle;
    private String fileUrl;

    public static FileResponseDto of(File file) {
        return FileResponseDto.builder()
                .fileTitle(file.getFileTitle())
                .fileUrl(file.getFileUrl())
                .build();
    }
}


