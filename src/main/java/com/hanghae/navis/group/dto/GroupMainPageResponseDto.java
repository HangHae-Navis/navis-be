package com.hanghae.navis.group.dto;

import com.hanghae.navis.common.entity.BasicBoard;
import com.hanghae.navis.group.entity.Group;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GroupMainPageResponseDto {
    private String groupName;
    private Page<BasicBoardResponseDto> basicBoards;

    public static GroupMainPageResponseDto of(Group group, Page<BasicBoard> basicBoardPage) {
        return GroupMainPageResponseDto.builder()
                .groupName(group.getGroupName())
                .basicBoards(BasicBoardResponseDto.toDtoPage(basicBoardPage))
                .build();
    }


}
