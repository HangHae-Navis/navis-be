package com.hanghae.navis.group.dto;


import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class GroupRequestDto {

    private String groupName;
    private String groupInfo;
    private MultipartFile groupImage;
}
