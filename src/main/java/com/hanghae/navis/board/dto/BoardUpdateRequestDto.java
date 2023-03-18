package com.hanghae.navis.board.dto;

import com.hanghae.navis.common.dto.HashtagRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class BoardUpdateRequestDto {
    private String subtitle;
    private String title;
    private String content;
    private Long important;
    private List<String> updateUrlList = new ArrayList<>();
    private List<String> hashtagList;

    private List<MultipartFile> multipartFiles;
}
