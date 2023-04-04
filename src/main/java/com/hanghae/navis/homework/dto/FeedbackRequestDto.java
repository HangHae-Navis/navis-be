package com.hanghae.navis.homework.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor
public class FeedbackRequestDto {
    @Size(max = 300)
    private String feedback;

    private boolean submitCheck;
}
