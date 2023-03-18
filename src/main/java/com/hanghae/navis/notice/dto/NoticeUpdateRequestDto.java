package com.hanghae.navis.notice.dto;

import com.hanghae.navis.board.dto.BoardUpdateRequestDto;
import com.hanghae.navis.common.dto.HashtagRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class NoticeUpdateRequestDto extends BoardUpdateRequestDto {
    private String subtitle;
    private String title;
    private String content;
    private String important;
    private List<String> updateUrlList = new ArrayList<>();
    private List<HashtagRequestDto> hashtagList;
}
