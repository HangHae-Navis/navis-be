package com.hanghae.navis.group.dto;

import com.hanghae.navis.common.entity.BasicBoard;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecentlyViewedDto {
    private Long id;
    private String title;
    private String dtype;

    public static RecentlyViewedDto of(BasicBoard basicBoard) {
        return RecentlyViewedDto.builder()
                .id(basicBoard.getId())
                .title(basicBoard.getTitle())
                .dtype(basicBoard.getDtype())
                .build();
    }
}
