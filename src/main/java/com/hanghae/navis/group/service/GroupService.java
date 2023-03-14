package com.hanghae.navis.group.service;

import com.hanghae.navis.common.dto.CustomException;
import com.hanghae.navis.common.dto.Message;
import com.hanghae.navis.common.entity.ExceptionMessage;
import com.hanghae.navis.common.entity.SuccessMessage;
import com.hanghae.navis.group.dto.GroupRequestDto;
import com.hanghae.navis.group.dto.ApplyRequestDto;
import com.hanghae.navis.group.entity.Group;
import com.hanghae.navis.group.entity.UserGroupList;
import com.hanghae.navis.group.entity.UserGroupRoleEnum;
import com.hanghae.navis.group.repository.GroupRepository;
import com.hanghae.navis.user.entity.User;
import com.hanghae.navis.user.repository.UserGroupListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class GroupService {

    private final GroupRepository groupRepository;
    private final UserGroupListRepository userGroupListRepository;

    @Transactional
    public ResponseEntity<Message> createGroup(GroupRequestDto requestDto, User user) {

        Group group = new Group(requestDto.getGroupName(), user);

        //그룹코드
        String groupCode = generateGroupCode();
        //그룹코드 중복시 다시 지정
        while(groupRepository.findByGroupCode(groupCode).isPresent()) {
            groupCode = generateGroupCode();
        }
        group.setGroupCode(groupCode);

        //설립자 소속 설정, 권한 ADMIN 설정
        UserGroupList userGroupList = new UserGroupList(user, group);
        userGroupList.setGroupRole(UserGroupRoleEnum.ADMIN);

        groupRepository.save(group);
        userGroupListRepository.save(userGroupList);

        return Message.toResponseEntity(SuccessMessage.GROUP_CREATE_SUCCESS);
    }

    @Transactional
    public ResponseEntity<Message> applyGroup(ApplyRequestDto requestDto, User user) {

        Optional<Group> gr = groupRepository.findByGroupCode(requestDto.getGroupCode());

        if(gr.isEmpty()) {
            throw new CustomException(ExceptionMessage.GROUP_NOT_FOUND);
        }

        Group group = gr.get();
        //이미 가입한 그룹일 경우 튕겨냄
        Optional<UserGroupList> ugl = userGroupListRepository.findByUserAndGroup(user, group);
        if(ugl.isPresent()) {
            throw new CustomException(ExceptionMessage.ALREADY_JOINED);
        }

        UserGroupList userGroupList = new UserGroupList(user, group);
        userGroupListRepository.save(userGroupList);

        return Message.toResponseEntity(SuccessMessage.GROUP_APPLY_SUCCESS);
    }




    private String generateGroupCode() {
        StringBuilder groupCode = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            Random random = new Random();
            char c = (char)(random.nextInt(26)+97);
            groupCode.append(c);
        }

        return groupCode.toString();
    }
}
