package com.hanghae.navis.vote.dto;

import com.hanghae.navis.board.entity.Board;
import com.hanghae.navis.common.dto.HashtagResponseDto;
import com.hanghae.navis.group.dto.GroupResponseDto;
import com.hanghae.navis.group.entity.Group;
import com.hanghae.navis.group.entity.GroupMember;
import com.hanghae.navis.vote.entity.Vote;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;


@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class VoteListResponseDto {
    private Long id;

    private String title;

    private String subtitle;

    private String content;

    private String nickName;
    private List<HashtagResponseDto> hashtagResponseDtoList;

    private LocalDateTime createAt;

    public static VoteListResponseDto of(Vote vote) {
        return VoteListResponseDto.builder()
                .id(vote.getId())
                .subtitle(vote.getSubtitle())
                .title(vote.getTitle())
                .content(vote.getContent())
                .nickName(vote.getUser().getNickname())
                .createAt(vote.getCreatedAt())
                .hashtagResponseDtoList(HashtagResponseDto.toDtoList(vote.getHashtagList()))
                .build();
    }

    public static Page<VoteListResponseDto> toDtoPage(Page<Vote> votePage) {
        return votePage.map(VoteListResponseDto::of);
    }

}
