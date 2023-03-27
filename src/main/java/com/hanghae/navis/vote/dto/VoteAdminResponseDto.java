package com.hanghae.navis.vote.dto;

import com.hanghae.navis.common.dto.BasicBoardResponseDto;
import com.hanghae.navis.common.dto.FileResponseDto;
import com.hanghae.navis.group.entity.GroupMemberRoleEnum;
import com.hanghae.navis.vote.entity.Vote;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class VoteAdminResponseDto extends BasicBoardResponseDto {

    private List<OptionAdminResponseDto> optionList;
    private LocalDateTime expirationTime;
    private boolean isExpiration;

    public static VoteAdminResponseDto of(Vote vote, List<FileResponseDto> fileList, List<String> hashtagList, List<OptionAdminResponseDto> optionList, boolean expiration, LocalDateTime expirationTime, GroupMemberRoleEnum role) {
        return VoteAdminResponseDto.builder()
                .id(vote.getId())
                .nickname(vote.getUser().getNickname())
                .fileList(fileList)
                .hashtagList(hashtagList)
                .title(vote.getTitle())
                .content(vote.getContent())
                .subtitle(vote.getSubtitle())
                .important(vote.getImportant())
                .createAt(vote.getCreatedAt())
                .optionList(optionList)
                .expirationTime(expirationTime)
                .isExpiration(expiration)
                .role(role)
                .build();
    }
}
