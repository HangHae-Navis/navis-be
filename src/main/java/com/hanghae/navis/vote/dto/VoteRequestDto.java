package com.hanghae.navis.vote.dto;

import com.hanghae.navis.board.dto.BoardRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class VoteRequestDto extends BoardRequestDto {

    private long expirationDate;
    private List<OptionRequestDto> optionRequestDto;

    public VoteRequestDto(String title, String content, String subtitle, long expirationDate, List<OptionRequestDto> optionRequestDto) {
        super(title, content, subtitle);
        this.expirationDate = expirationDate;
        this.optionRequestDto = optionRequestDto;
    }
}
