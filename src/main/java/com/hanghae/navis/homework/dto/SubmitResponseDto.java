package com.hanghae.navis.homework.dto;

import com.hanghae.navis.homework.entity.Homework;
import com.hanghae.navis.homework.entity.HomeworkSubject;
import com.hanghae.navis.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubmitResponseDto {
    private Long id;
    private String nickname;
    private boolean submit;
    private List<HomeworkFileResponseDto> fileList;

    public static SubmitResponseDto of(HomeworkSubject homeworkSubject, List<HomeworkFileResponseDto> fileList) {
        return SubmitResponseDto.builder()
                .id(homeworkSubject.getId())
                .submit(homeworkSubject.isSubmit())
                .nickname(homeworkSubject.getUser().getNickname())
                .fileList(fileList)
                .build();
    }
}
