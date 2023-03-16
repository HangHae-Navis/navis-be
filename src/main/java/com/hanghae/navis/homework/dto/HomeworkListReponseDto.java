package com.hanghae.navis.homework.dto;

import com.hanghae.navis.homework.entity.Homework;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class HomeworkListReponseDto {
    private Long id;

    private String title;

    private String subtitle;

    private String content;

    private String nickName;

    private String groupName;

    private LocalDateTime createAt;

    private LocalDateTime expirationDate;

    public HomeworkListReponseDto(Homework homework) {
        this.id = homework.getId();
        this.title = homework.getTitle();
        this.subtitle = homework.getSubtitle();
        this.content = homework.getContent();
        this.nickName = homework.getUser().getNickname();
        this.groupName = homework.getGroup().getGroupName();
        this.createAt = homework.getCreatedAt();
        this.expirationDate = homework.getExpirationDate();
    }
}
