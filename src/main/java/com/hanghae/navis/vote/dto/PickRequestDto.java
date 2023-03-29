package com.hanghae.navis.vote.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class PickRequestDto {
    private Long voteOption;

    public PickRequestDto(Long voteOption) {
        this.voteOption = voteOption;
    }
}
