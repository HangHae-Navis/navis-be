package com.hanghae.navis.homework.dto;

import com.hanghae.navis.homework.entity.HomeworkSubjectFile;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HomeworkFileResponseDto {
    private String fileName;
    private String fileUrl;

    public static HomeworkFileResponseDto of(HomeworkSubjectFile file) {
        return HomeworkFileResponseDto.builder()
                .fileName(file.getFileName())
                .fileUrl(file.getFileUrl())
                .build();
    }
}
