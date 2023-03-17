package com.hanghae.navis.vote.dto;

import com.hanghae.navis.board.dto.BoardRequestDto;
import com.hanghae.navis.board.dto.HashtagRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class VoteRequestDto extends BoardRequestDto {

    private long expirationDate;
    private List<OptionRequestDto> optionRequestDto;
//    private List<String> updateUrlList = new ArrayList<>();


    //생성
    public VoteRequestDto(String title, String content, String subtitle, List<HashtagRequestDto> hashtagList, long expirationDate, List<OptionRequestDto> optionRequestDto) {
        super(title, content, subtitle, hashtagList);
        this.expirationDate = expirationDate;
        this.optionRequestDto = optionRequestDto;
    }

    //업데이트
//    public VoteRequestDto(String title, String content, String subtitle, long expirationDate, List<OptionRequestDto> optionRequestDto, List<String> updateUrlList) {
//        super(title, content, subtitle);
//        this.expirationDate = expirationDate;
//        this.optionRequestDto = optionRequestDto;
//        this.updateUrlList = updateUrlList;
//    }
}
