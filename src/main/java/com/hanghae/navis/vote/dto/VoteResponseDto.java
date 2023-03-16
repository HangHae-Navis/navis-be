package com.hanghae.navis.vote.dto;

import com.hanghae.navis.board.dto.BoardResponseDto;
import com.hanghae.navis.board.dto.FileResponseDto;
import com.hanghae.navis.vote.entity.Vote;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class VoteResponseDto extends BoardResponseDto {

    private List<OptionResponseDto> optionList;

    public VoteResponseDto(Vote vote, List<FileResponseDto> fileList, List<OptionResponseDto> optionList) {
        super(vote, fileList);
        this.optionList = optionList;
    }
}
