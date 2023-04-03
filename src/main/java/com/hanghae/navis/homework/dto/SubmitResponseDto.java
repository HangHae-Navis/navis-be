package com.hanghae.navis.homework.dto;

import com.hanghae.navis.homework.entity.Feedback;
import com.hanghae.navis.homework.entity.Homework;
import com.hanghae.navis.homework.entity.HomeworkSubject;
import com.hanghae.navis.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubmitResponseDto {
    private Long id;
    private String nickname;
    private boolean submit;
    private boolean late;
    private LocalDateTime createdAt;
    private List<HomeworkFileResponseDto> fileList;
    private boolean submitCheck;
    private List<String> feedbackList;

    public static SubmitResponseDto of(HomeworkSubject homeworkSubject, List<HomeworkFileResponseDto> fileList, List<String> feedbackList) {
        return SubmitResponseDto.builder()
                .id(homeworkSubject.getId())
                .submit(homeworkSubject.isSubmit())
                .late(homeworkSubject.isLate())
                .createdAt(homeworkSubject.getCreatedAt())
                .nickname(homeworkSubject.getUser().getNickname())
                .fileList(fileList)
                .submitCheck(homeworkSubject.isSubmitCheck())
                .feedbackList(feedbackList)
                .build();
    }
}
