package com.hanghae.navis.homework.dto;

import com.hanghae.navis.board.dto.BoardResponseDto;
import com.hanghae.navis.board.dto.FileResponseDto;
import com.hanghae.navis.board.dto.HashtagResponseDto;
import com.hanghae.navis.homework.entity.Homework;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
public class HomeworkResponseDto extends BoardResponseDto {
    private LocalDateTime expirationTime;
    private boolean expiration;

    public HomeworkResponseDto(Homework homework, List<FileResponseDto> fileList, List<HashtagResponseDto> hashtagList, boolean expiration, LocalDateTime expirationTime) {
        super(homework, fileList, hashtagList);
        this.expiration = expiration;
        this.expirationTime = expirationTime;
    }
}
