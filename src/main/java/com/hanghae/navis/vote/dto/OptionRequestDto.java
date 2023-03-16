package com.hanghae.navis.vote.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class OptionRequestDto {
    private String option;

    public OptionRequestDto(String option) {
        this.option = option;
    }
}
