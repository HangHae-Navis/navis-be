package com.hanghae.navis.notification.entity;

import lombok.Getter;

@Getter
public enum NotificationType {

    HOMEWORK_POST("homework", "새로운 과제가 올라왔습니다."),
    NOTICE_POST("homework", "새로운 공지가 올라왔습니다."),
    SURVEY_POST("survey", "새로운 설문이 올라왔습니다."),
    VOTE_POST("vote", "새로운 투표가 올라왔습니다."),
    CHAT_POST("chat", "새로운 채팅이 왔습니다.");

    private final String type;
    private final String content;

    NotificationType(String type, String content) {
        this.type = type;
        this.content = content;
    }
}
