package com.hanghae.navis.group.dto;


import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Size;

@Getter
@Setter
public class GroupRequestDto {

    @Size(min = 3, max = 20)
    private String groupName;
    @Size(min = 1, max = 20)
    private String groupInfo;
    private MultipartFile groupImage;
}
