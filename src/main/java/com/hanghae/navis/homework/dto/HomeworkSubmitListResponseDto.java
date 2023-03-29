package com.hanghae.navis.homework.dto;

import java.time.LocalDateTime;

public interface HomeworkSubmitListResponseDto {
    Long getUserId();
    String getNickname();
    Boolean getSubmit();
    String getFileUrl();
    LocalDateTime getCreatedAt();
    Boolean getLate();
}
