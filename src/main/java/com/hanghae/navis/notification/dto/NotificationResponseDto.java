package com.hanghae.navis.notification.dto;

import com.hanghae.navis.common.dto.FileResponseDto;
import com.hanghae.navis.group.entity.GroupMemberRoleEnum;
import com.hanghae.navis.notice.dto.NoticeResponseDto;
import com.hanghae.navis.notice.entity.Notice;
import com.hanghae.navis.notification.entity.Notification;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationResponseDto {

    private Long id;
    private String type;
    private String content;
    private String url;
    private boolean isRead;

    public static NotificationResponseDto of(Notification notification) {
        return NotificationResponseDto.builder()
                .id(notification.getId())
                .type(notification.getNotificationType().getType())
                .content(notification.getContent())
                .url(notification.getUrl())
                .isRead(notification.getIsRead())
                .build();
    }
}
