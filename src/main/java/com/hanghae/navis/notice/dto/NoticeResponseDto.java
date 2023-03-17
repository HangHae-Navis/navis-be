package com.hanghae.navis.notice.dto;

import com.hanghae.navis.common.dto.BasicBoardResponseDto;
import com.hanghae.navis.common.dto.FileResponseDto;
import com.hanghae.navis.common.dto.HashtagResponseDto;
import com.hanghae.navis.notice.entity.Notice;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@NoArgsConstructor
@SuperBuilder
public class NoticeResponseDto extends BasicBoardResponseDto {
    public static NoticeResponseDto of(Notice notice, List<FileResponseDto> fileList, List<HashtagResponseDto> hashtagList) {
        return NoticeResponseDto.builder()
                .id(notice.getId())
                .nickname(notice.getUser().getNickname())
                .fileList(fileList)
                .title(notice.getTitle())
                .content(notice.getContent())
                .subtitle(notice.getSubtitle())
                .createAt(notice.getCreatedAt())
                .hashtagList(hashtagList)
                .build();
    }
}