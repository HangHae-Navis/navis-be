package com.hanghae.navis.common.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public abstract class BasicBoardResponseDto {
    private Long id;
    private String nickname;
    private List<FileResponseDto> fileList;
    private String title;
    private String content;
    private String subtitle;
    private Long important;
    private LocalDateTime createAt;
    private List<String> hashtagList;
}
