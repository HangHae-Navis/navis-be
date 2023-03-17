package com.hanghae.navis.board.dto;

import com.hanghae.navis.common.entity.File;
import com.hanghae.navis.vote.dto.OptionResponseDto;
import com.hanghae.navis.vote.entity.VoteOption;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FileResponseDto {

    private String fileTitle;
    private String fileUrl;

    public FileResponseDto(File file) {
        this.fileTitle = file.getFileTitle();
        this.fileUrl = file.getFileUrl();
    }
    public static FileResponseDto of(File file) {
        return FileResponseDto.builder()
                .fileTitle(file.getFileTitle())
                .fileUrl(file.getFileUrl())
                .build();
    }
}


