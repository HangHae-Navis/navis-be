package com.hanghae.navis.board.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class BoardUpdateRequestDto {
    @Size(min = 1, max = 50)
    private String subtitle;

    @Size(min = 1, max = 50)
    private String title;

    @Size(min = 1, max = 3000)
    private String content;

    private Long important;

    private List<String> updateUrlList = new ArrayList<>();

    private List<String> hashtagList;

    private List<MultipartFile> multipartFiles;
}
