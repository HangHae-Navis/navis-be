package com.hanghae.navis.group.dto;

import com.hanghae.navis.common.entity.BasicBoard;
import com.hanghae.navis.group.entity.Group;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GroupMainPageResponseDto {
    private String groupName;
    private String groupInfo;
    private String groupCode;
    private boolean isAdmin;
    private Page<MainPageBasicBoardDto> basicBoards;

    public static GroupMainPageResponseDto of(Group group, boolean isAdmin, Page<BasicBoard> basicBoardPage) {
        return GroupMainPageResponseDto.builder()
                .groupName(group.getGroupName())
                .groupInfo(group.getGroupInfo())
                .groupCode(group.getGroupCode())
                .isAdmin(isAdmin)
                .basicBoards(MainPageBasicBoardDto.toDtoPage(basicBoardPage))
                .build();
    }


}
