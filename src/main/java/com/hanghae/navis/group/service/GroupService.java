package com.hanghae.navis.group.service;

import com.hanghae.navis.board.repository.BoardRepository;
import com.hanghae.navis.common.config.S3Uploader;
import com.hanghae.navis.common.dto.CustomException;
import com.hanghae.navis.common.dto.Message;
import com.hanghae.navis.common.entity.BasicBoard;
import com.hanghae.navis.common.entity.ExceptionMessage;
import com.hanghae.navis.common.entity.SuccessMessage;
import com.hanghae.navis.common.repository.BasicBoardRepository;
import com.hanghae.navis.group.dto.*;
import com.hanghae.navis.group.entity.Group;
import com.hanghae.navis.group.entity.GroupMember;
import com.hanghae.navis.group.entity.GroupMemberRoleEnum;
import com.hanghae.navis.group.repository.GroupRepository;
import com.hanghae.navis.user.entity.User;
import com.hanghae.navis.user.repository.UserRepository;
import com.hanghae.navis.group.repository.GroupMemberRepository;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;
import java.util.Random;

@Service
@Slf4j
@RequiredArgsConstructor
public class GroupService {

    private final UserRepository userRepository;
    private final GroupRepository groupRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final BasicBoardRepository basicBoardRepository;
    private final BoardRepository boardRepository;
    private final S3Uploader s3Uploader;

    @Transactional
    public ResponseEntity<Message> createGroup(@RequestBody GroupRequestDto requestDto, User user) {

        Group group = new Group(requestDto, user);
        MultipartFile multipartFile = requestDto.getGroupImage();

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

    @Transactional(readOnly = true)
    public ResponseEntity<Message> getGroups(int page, int size, String category, User user) {
        Pageable pageable = PageRequest.of(page, size);

        Page<GroupMember> groupMemberPage;

        switch (category) {
            case "joined":
                groupMemberPage = groupMemberRepository.findAllByUserAndGroupRoleIsNot(user, GroupMemberRoleEnum.ADMIN, pageable);
                break;
            case "myGroups":
                groupMemberPage = groupMemberRepository.findAllByUserAndGroupRole(user, GroupMemberRoleEnum.ADMIN, pageable);
                break;
            case "all":
                groupMemberPage = groupMemberRepository.findAllByUser(user, pageable);
                break;
            default:
                throw new CustomException(ExceptionMessage.INVALID_CATEGORY);
        }

        Page<GroupResponseDto> groupResponseDtoPage = GroupResponseDto.toDtoPage(groupMemberPage);

        return Message.toResponseEntity(SuccessMessage.GROUPS_GET_SUCCESS, groupResponseDtoPage);

    }

    @Transactional(readOnly = true)
    public ResponseEntity<Message> getGroupDetails(Long groupId, User user) {
        Group group = groupRepository.findById(groupId).orElseThrow(
                () -> new CustomException(ExceptionMessage.GROUP_NOT_FOUND)
        );

        GroupMemberRoleEnum role = groupMemberRepository.findByUserAndGroup(user, group).get().getGroupRole();

        if(!role.equals(GroupMemberRoleEnum.ADMIN)) {
            throw new CustomException(ExceptionMessage.ADMIN_ONLY);
        }

        GroupDetailsResponseDto responseDto = GroupDetailsResponseDto.of(group);

        return Message.toResponseEntity(SuccessMessage.GROUP_DETAILS_GET_SUCCESS, responseDto);
    }

    @Transactional(readOnly = true)
    public ResponseEntity<Message> getGroupMainPage(Long groupId, int page, int size, String category, User user) {
        Group group = groupRepository.findById(groupId).orElseThrow(
                () -> new CustomException(ExceptionMessage.GROUP_NOT_FOUND)
        );

        GroupMember groupMember = groupMemberRepository.findByUserAndGroup(user, group).orElseThrow(
                () -> new CustomException(ExceptionMessage.GROUP_NOT_JOINED)
        );

        boolean isAdmin = groupMember.getGroupRole().equals(GroupMemberRoleEnum.ADMIN);

        Pageable pageable = PageRequest.of(page, size);

        Page<BasicBoard> basicBoardPage;

        if (category.equals("all")) {
            basicBoardPage = basicBoardRepository.findAllByGroupOrderByCreatedAtDesc(group, pageable);
        } else if(category.equals("board") || category.equals("vote") || category.equals("homework")) {
            basicBoardPage = basicBoardRepository.findAllByGroupAndDtypeOrderByCreatedAtDesc(group, category, pageable);
        } else {
            throw new CustomException(ExceptionMessage.INVALID_CATEGORY);
        }

        GroupMainPageResponseDto responseDto = GroupMainPageResponseDto.of(group, isAdmin, basicBoardPage);

        return Message.toResponseEntity(SuccessMessage.GROUP_MAIN_PAGE_GET_SUCCESS, responseDto);
    }

    @Transactional
    public ResponseEntity<Message> deleteGroupMember(Long groupId, Long memberId, User user) {
        Group group = groupRepository.findById(groupId).orElseThrow(
                () -> new CustomException(ExceptionMessage.GROUP_NOT_FOUND)
        );

        GroupMember groupMember = groupMemberRepository.findByUserAndGroup(user, group).orElseThrow(
            () -> new CustomException(ExceptionMessage.GROUP_NOT_JOINED)
        );

        if(memberId != null) {
            if(!groupMember.getGroupRole().equals(GroupMemberRoleEnum.ADMIN)) {
                throw new CustomException(ExceptionMessage.ADMIN_ONLY);
            }
            groupMemberRepository.deleteById(memberId);
            return Message.toResponseEntity(SuccessMessage.MEMBER_DELETE_SUCCESS);
        }

        if(groupMember.getGroupRole().equals(GroupMemberRoleEnum.ADMIN)) {
            throw new CustomException(ExceptionMessage.ADMIN_CANNOT_QUIT);
        }

        groupMemberRepository.delete(groupMember);
        return Message.toResponseEntity(SuccessMessage.GROUP_QUIT_SUCCESS);
    }

    @Transactional
    public ResponseEntity<Message> deleteGroup(Long groupId, User user) {
        Group group = groupRepository.findById(groupId).orElseThrow(
                () -> new CustomException(ExceptionMessage.GROUP_NOT_FOUND)
        );

        GroupMember groupMember = groupMemberRepository.findByUserAndGroup(user, group).orElseThrow(
                () -> new CustomException(ExceptionMessage.GROUP_NOT_JOINED)
        );

        if(!groupMember.getGroupRole().equals(GroupMemberRoleEnum.ADMIN)) {
            throw new CustomException(ExceptionMessage.ADMIN_ONLY);
        }

        groupRepository.deleteById(groupId);
        return Message.toResponseEntity(SuccessMessage.GROUP_DELETE_SUCCESS);
    }
}

