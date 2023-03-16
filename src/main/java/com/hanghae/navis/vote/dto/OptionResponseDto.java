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
public class OptionResponseDto {

    private String option;
    private Long count;

    public OptionResponseDto(String option, Long count) {
        this.option = option;
        this.count = count;
    }
}
