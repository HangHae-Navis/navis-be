package com.hanghae.navis.homework.dto;

import com.hanghae.navis.homework.entity.HomeworkSubject;
import com.hanghae.navis.homework.entity.HomeworkSubjectFile;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
@Builder
public class SubmitMemberResponseDto {
    private Long id;
    private String nickname;
    private boolean submit;
    private List<String> fileList;
    private LocalDateTime createdAt;
    private boolean late;

    public static SubmitMemberResponseDto of(HomeworkSubmitListResponseDto responseDto) {
        return SubmitMemberResponseDto.builder()
                .id(responseDto.getUserId())
                .nickname(responseDto.getNickname())
                .submit(responseDto.getSubmit())
                .fileList(Arrays.asList(responseDto.getFileUrl().split(", ")))
                .createdAt(responseDto.getCreatedAt())
                .late(responseDto.getLate())
                .build();
    }
}
