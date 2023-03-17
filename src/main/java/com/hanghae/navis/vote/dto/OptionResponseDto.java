package com.hanghae.navis.vote.dto;

import com.hanghae.navis.board.dto.BoardResponseDto;
import com.hanghae.navis.board.dto.FileResponseDto;
import com.hanghae.navis.vote.entity.Vote;
import com.hanghae.navis.vote.entity.VoteOption;
import lombok.*;
import org.springframework.data.domain.Page;

import java.util.List;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OptionResponseDto {

    private String option;
    private Long count;

    public static OptionResponseDto of(VoteOption voteOption) {
        return OptionResponseDto.builder()
                .option(voteOption.getOption())
                .count(voteOption.getVoteRecordList() == null ? 0L : (long) voteOption.getVoteRecordList().size())
                .build();
    }
}
