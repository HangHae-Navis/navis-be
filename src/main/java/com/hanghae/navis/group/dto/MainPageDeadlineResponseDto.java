package com.hanghae.navis.group.dto;


import com.hanghae.navis.homework.entity.Homework;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MainPageDeadlineResponseDto {
    private Long id;
    private String title;
    private String nickname;
    private LocalDateTime deadline;

    public static MainPageDeadlineResponseDto of(Homework homework) {
        return MainPageDeadlineResponseDto.builder()
                .id(homework.getId())
                .title(homework.getTitle())
                .nickname(homework.getUser().getNickname())
                .deadline(homework.getExpirationDate())
                .build();
    }

    public static List<MainPageDeadlineResponseDto>  toDtoList(List<Homework> homeworkList) {
        return homeworkList.stream().map(MainPageDeadlineResponseDto::of).collect(Collectors.toList());
    }
}
