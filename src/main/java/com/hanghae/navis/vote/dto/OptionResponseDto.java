package com.hanghae.navis.vote.dto;

import com.hanghae.navis.vote.entity.VoteOption;
import lombok.*;

import java.util.List;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OptionResponseDto {

    private Long optionId;
    private String option;
    private Long count;

    public static OptionResponseDto of(VoteOption voteOption) {
        return OptionResponseDto.builder()
                .optionId(voteOption.getId())
                .option(voteOption.getOption())
                .count(voteOption.getVoteRecordList() == null ? 0L : (long) voteOption.getVoteRecordList().size())
                .build();
    }
}
