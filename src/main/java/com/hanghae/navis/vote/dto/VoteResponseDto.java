package com.hanghae.navis.vote.dto;

import com.hanghae.navis.common.dto.FileResponseDto;
import com.hanghae.navis.common.dto.BasicBoardResponseDto;
import com.hanghae.navis.group.dto.RecentlyViewedDto;
import com.hanghae.navis.group.entity.GroupMemberRoleEnum;
import com.hanghae.navis.vote.entity.Vote;
import lombok.*;
import lombok.experimental.SuperBuilder;

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
    private Long myPick;

    public static VoteResponseDto of(Vote vote, List<FileResponseDto> fileList, List<String> hashtagList, List<OptionResponseDto> optionList,
                                     boolean expiration, LocalDateTime expirationTime, GroupMemberRoleEnum role, Long myPick, List<RecentlyViewedDto> rv,
                                     GroupMemberRoleEnum authorRole, boolean isAuthor) {
        return VoteResponseDto.builder()
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
                .myPick(myPick)
                .recentlyViewed(rv)
                .authorRole(authorRole)
                .isAuthor(isAuthor)
                .build();
    }
}
