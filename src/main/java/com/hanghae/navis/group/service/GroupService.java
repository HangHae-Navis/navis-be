package com.hanghae.navis.group.service;

import com.hanghae.navis.common.config.S3Uploader;
import com.hanghae.navis.common.dto.CustomException;
import com.hanghae.navis.common.dto.Message;
import com.hanghae.navis.common.entity.ExceptionMessage;
import com.hanghae.navis.common.entity.SuccessMessage;
import com.hanghae.navis.group.dto.GroupRequestDto;
import com.hanghae.navis.group.dto.ApplyRequestDto;
import com.hanghae.navis.group.dto.GroupResponseDto;
import com.hanghae.navis.group.entity.Group;
import com.hanghae.navis.group.entity.GroupMember;
import com.hanghae.navis.group.entity.GroupMemberRoleEnum;
import com.hanghae.navis.group.repository.GroupRepository;
import com.hanghae.navis.user.entity.User;
import com.hanghae.navis.user.repository.GroupMemberRepository;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class GroupService {

    private final GroupRepository groupRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final S3Uploader s3Uploader;

    @Transactional
    public ResponseEntity<Message> createGroup(@RequestBody GroupRequestDto requestDto, User user) {

        Group group = new Group(requestDto, user);
        MultipartFile multipartFile = requestDto.getMultipartFile();

        if(!(multipartFile == null || multipartFile.isEmpty())) {
            try{
                String groupImage = s3Uploader.upload(multipartFile);
                group.addGroupImage(groupImage);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        //그룹코드
        String groupCode = generateGroupCode();
        //그룹코드 중복시 다시 지정
        while (groupRepository.findByGroupCode(groupCode).isPresent()) {
            groupCode = generateGroupCode();
        }
        group.setGroupCode(groupCode);

        //설립자 소속 설정, 권한 ADMIN 설정
        GroupMember groupMember = new GroupMember(user, group);
        groupMember.setGroupRole(GroupMemberRoleEnum.ADMIN);

        groupRepository.save(group);
        groupMemberRepository.save(groupMember);

        return Message.toResponseEntity(SuccessMessage.GROUP_CREATE_SUCCESS);
    }

    @Transactional
    public ResponseEntity<Message> applyGroup(ApplyRequestDto requestDto, User user) {

        Optional<Group> gr = groupRepository.findByGroupCode(requestDto.getGroupCode());

        if (gr.isEmpty()) {
            throw new CustomException(ExceptionMessage.GROUP_NOT_FOUND);
        }

        Group group = gr.get();
        //이미 가입한 그룹일 경우 튕겨냄
        Optional<GroupMember> ugl = groupMemberRepository.findByUserAndGroup(user, group);
        if (ugl.isPresent()) {
            throw new CustomException(ExceptionMessage.ALREADY_JOINED);
        }

        GroupMember groupMember = new GroupMember(user, group);
        groupMemberRepository.save(groupMember);

        return Message.toResponseEntity(SuccessMessage.GROUP_APPLY_SUCCESS);
    }


    private String generateGroupCode() {
        StringBuilder groupCode = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            Random random = new Random();
            char c = (char) (random.nextInt(26) + 97);
            groupCode.append(c);
        }

        return groupCode.toString();
    }

    public ResponseEntity<Message> getGroups(int page, int size, String category, User user) {
        Pageable pageable = PageRequest.of(page, size);

        Page<GroupMember> groupMemberPage;

        if (category.equals("joined")) {
            groupMemberPage = groupMemberRepository.findAllByUserAndGroupRole(user, GroupMemberRoleEnum.USER, pageable);
        } else if (category.equals("myGroups")) {
            groupMemberPage = groupMemberRepository.findAllByUserAndGroupRole(user, GroupMemberRoleEnum.ADMIN, pageable);
        } else if (category.equals("all")) {
            groupMemberPage = groupMemberRepository.findAllByUser(user, pageable);
        } else {
            throw new CustomException(ExceptionMessage.INVALID_CATEGORY);
        }

        Page<GroupResponseDto> groupResponseDtoPage = GroupResponseDto.toDtoPage(groupMemberPage);

        return Message.toResponseEntity(SuccessMessage.GROUPS_GET_SUCCESS, groupResponseDtoPage);

    }
}

