package com.hanghae.navis.homework.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class SubmitMemberResponseDto {
    private Long id;
    private String nickname;
    private boolean submit;
    private Long subjectId;
    private List<String> fileList;
    private List<String> fileName;
    private List<String> feedbackList;
    private boolean updateSubject;
    private boolean submitCheck;
    private LocalDateTime createdAt;
    private boolean late;

    public static SubmitMemberResponseDto of(HomeworkSubmitListResponseDto responseDto) {
        return SubmitMemberResponseDto.builder()
                .id(responseDto.getUserId())
                .nickname(responseDto.getNickname())
                .submit(responseDto.getSubmit())
                .subjectId(responseDto.getSubjectId())
                .fileList(Arrays.asList(responseDto.getFileUrl().split(", ")))
                .fileName(Arrays.asList(responseDto.getFileName().split(", ")))
                .createdAt(responseDto.getCreatedAt())
                .late(responseDto.getLate())
                .feedbackList(Arrays.asList(responseDto.getFeedback().split(", ")))
                .updateSubject(responseDto.getUpdateSubject())
                .submitCheck(responseDto.getSubmitCheck())
                .build();
    }
}
