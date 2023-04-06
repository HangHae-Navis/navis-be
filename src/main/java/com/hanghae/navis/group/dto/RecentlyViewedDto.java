package com.hanghae.navis.group.dto;

import com.hanghae.navis.common.entity.BasicBoard;
import com.hanghae.navis.group.entity.RecentlyViewed;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

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
