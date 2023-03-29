//package com.hanghae.navis.homework.dto;
//
//import com.hanghae.navis.group.entity.GroupMember;
//import com.hanghae.navis.homework.entity.Homework;
//import com.hanghae.navis.homework.entity.HomeworkSubject;
//import com.hanghae.navis.homework.entity.HomeworkSubjectFile;
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Getter;
//import org.springframework.data.domain.Page;
//
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.Set;
//import java.util.stream.Collectors;
//
//@Getter
//@AllArgsConstructor
//@Builder
//public class SubmitListResponseDto {
//    private Long id;
//    private String nickname;
//    private Boolean submit;
//    private Boolean late;
//    private LocalDateTime createdAt;
//    private List<String> fileList;
//
//    public static SubmitListResponseDto of(GroupMember groupMember) {
//        return SubmitListResponseDto.builder()
//                .id(groupMember.getId())
//                .nickname(groupMember.getUser().getNickname())
//                .build();
//    }
//
//    public static List<SubmitListResponseDto> toDtoList(Set<GroupMember> groupMemberList) {
//        return groupMemberList.stream().map(SubmitListResponseDto::of).collect(Collectors.toList());
//    }
//}
