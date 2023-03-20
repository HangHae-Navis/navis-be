package com.hanghae.navis.group.dto;

import com.hanghae.navis.common.entity.BasicBoard;
import com.hanghae.navis.group.entity.Group;
import com.hanghae.navis.homework.entity.Homework;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GroupMainPageResponseDto {
    private String groupName;
    private String groupInfo;
    private String groupCode;
    private boolean isAdmin;
    private List<MainPageDeadlineResponseDto> deadlines;
    private Page<MainPageBasicBoardDto> basicBoards;

    public static GroupMainPageResponseDto of(Group group, boolean isAdmin, List<Homework> homeworkList, Page<BasicBoard> basicBoardPage) {
        return GroupMainPageResponseDto.builder()
                .groupName(group.getGroupName())
                .groupInfo(group.getGroupInfo())
                .groupCode(group.getGroupCode())
                .isAdmin(isAdmin)
                .deadlines(MainPageDeadlineResponseDto.toDtoList(homeworkList))
                .basicBoards(MainPageBasicBoardDto.toDtoPage(basicBoardPage))
                .build();
    }


}
