package com.hanghae.navis.vote.dto;

import com.hanghae.navis.board.dto.BoardRequestDto;
import com.hanghae.navis.common.dto.HashtagRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class VoteRequestDto extends BoardRequestDto {

    private long expirationDate;
    private String optionList;
//    private List<String> updateUrlList = new ArrayList<>();


    //생성
    public VoteRequestDto(String title, String content, String subtitle, String important, String hashtagList, long expirationDate, String optionList, List<MultipartFile> multipartFiles) {
        super(title, content, subtitle, important, hashtagList, multipartFiles);
        this.expirationDate = expirationDate;
        this.optionList = optionList;
    }

    //업데이트
//    public VoteRequestDto(String title, String content, String subtitle, long expirationDate, List<OptionRequestDto> optionRequestDto, List<String> updateUrlList) {
//        super(title, content, subtitle);
//        this.expirationDate = expirationDate;
//        this.optionRequestDto = optionRequestDto;
//        this.updateUrlList = updateUrlList;
//    }
}
