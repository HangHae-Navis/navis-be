package com.hanghae.navis.board.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class BoardUpdateRequestDto {
    private String subtitle;
    private String title;
    private String content;
    private List<String> updateUrlList = new ArrayList<>();
}
