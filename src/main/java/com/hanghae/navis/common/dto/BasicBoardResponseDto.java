package com.hanghae.navis.common.dto;

import com.hanghae.navis.group.dto.RecentlyViewedDto;
import com.hanghae.navis.group.entity.GroupMemberRoleEnum;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public abstract class BasicBoardResponseDto {
    private Long id;
    private String nickname;
    private List<FileResponseDto> fileList;
    private String title;
    private String content;
    private String subtitle;
    private Long important;
    private LocalDateTime createAt;
    private List<String> hashtagList;
    private GroupMemberRoleEnum role;
    private GroupMemberRoleEnum authorRole;
    private boolean isAuthor;
    private List<RecentlyViewedDto> recentlyViewed;
}
