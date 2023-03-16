package com.hanghae.navis.vote.dto;

import com.hanghae.navis.board.dto.BoardResponseDto;
import com.hanghae.navis.board.dto.FileResponseDto;
import com.hanghae.navis.vote.entity.Vote;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.joda.time.DateTime;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class VoteResponseDto extends BoardResponseDto {

    private List<OptionResponseDto> optionList;

    private LocalDateTime expirationTime;
    private boolean expiration;

    public VoteResponseDto(Vote vote, List<FileResponseDto> fileList, List<OptionResponseDto> optionList, boolean expiration, LocalDateTime expirationTime) {
        super(vote, fileList);
        this.optionList = optionList;
        this.expirationTime = expirationTime;
        this.expiration = expiration;
    }
}
