
package com.hanghae.navis.vote.dto;

import com.hanghae.navis.vote.entity.VoteOption;
import lombok.*;

import java.util.List;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OptionAdminResponseDto {

    private Long optionId;
    private String option;
    private Long count;
    private List<PickUserInfoDto> pickUserInfoDtoList;

    public static OptionAdminResponseDto of(VoteOption voteOption, List<PickUserInfoDto> pickUserInfoDtoList) {
        return OptionAdminResponseDto.builder()
                .optionId(voteOption.getId())
                .option(voteOption.getOption())
                .count(voteOption.getVoteRecordList() == null ? 0L : (long) voteOption.getVoteRecordList().size())
                .pickUserInfoDtoList(pickUserInfoDtoList)
                .build();
    }
}
