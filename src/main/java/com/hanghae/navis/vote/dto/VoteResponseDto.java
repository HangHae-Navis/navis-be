package com.hanghae.navis.vote.dto;

import com.hanghae.navis.board.dto.BoardResponseDto;
import com.hanghae.navis.board.dto.FileResponseDto;
import com.hanghae.navis.common.dto.BasicBoardResponseDto;
import com.hanghae.navis.vote.entity.Vote;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.joda.time.DateTime;
import org.springframework.data.domain.Page;

import javax.annotation.security.DenyAll;
import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class VoteResponseDto extends BasicBoardResponseDto {

    private List<OptionResponseDto> optionList;
    private LocalDateTime expirationTime;
    private boolean isExpiration;


    public static VoteResponseDto of(Vote vote, List<FileResponseDto> fileList, List<OptionResponseDto> optionList, boolean expiration, LocalDateTime expirationTime) {
        return VoteResponseDto.builder()
                .id(vote.getId())
                .nickname(vote.getUser().getNickname())
                .fileList(fileList)
                .title(vote.getTitle())
                .content(vote.getContent())
                .subtitle(vote.getSubtitle())
                .createAt(vote.getCreatedAt())
                .optionList(optionList)
                .expirationTime(expirationTime)
                .isExpiration(expiration)
                .build();
    }
}
