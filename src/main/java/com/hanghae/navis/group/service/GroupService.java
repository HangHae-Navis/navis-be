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
import com.hanghae.navis.group.entity.BannedGroupMember;
import com.hanghae.navis.group.entity.Group;
import com.hanghae.navis.group.entity.GroupMember;
import com.hanghae.navis.group.entity.GroupMemberRoleEnum;
import com.hanghae.navis.group.repository.BannedGroupMemberRepository;
import com.hanghae.navis.group.repository.GroupRepository;
import com.hanghae.navis.homework.entity.Homework;
import com.hanghae.navis.homework.repository.HomeworkRepository;
import com.hanghae.navis.user.entity.User;
import com.hanghae.navis.user.repository.UserRepository;
import com.hanghae.navis.group.repository.GroupMemberRepository;
import com.hanghae.navis.vote.repository.VoteRepository;
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
import java.net.URLDecoder;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class GroupService {

    private final UserRepository userRepository;
    private final GroupRepository groupRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final BannedGroupMemberRepository bannedGroupMemberRepository;
    private final BasicBoardRepository basicBoardRepository;
    private final HomeworkRepository homeworkRepository;
    private final VoteRepository voteRepository;
    private final S3Uploader s3Uploader;

    @Transactional
    public ResponseEntity<Message> createGroup(@RequestBody GroupRequestDto requestDto, User user) {

        Group group = new Group(requestDto, user);
        MultipartFile multipartFile = requestDto.getGroupImage();

        if(!(multipartFile == null || multipartFile.isEmpty())) {
            try {
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

        return Message.toResponseEntity(SuccessMessage.GROUP_CREATE_SUCCESS, group.getId());
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

        //차단당한 그룹일 경우 튕겨냄
        Optional<BannedGroupMember> bgm = bannedGroupMemberRepository.findByUserAndGroup(user, group);
        if(bgm.isPresent()) {
            throw new CustomException(ExceptionMessage.BANNED_GROUP);
        }

        GroupMember groupMember = new GroupMember(user, group);
        groupMemberRepository.save(groupMember);

        return Message.toResponseEntity(SuccessMessage.GROUP_APPLY_SUCCESS, group.getId());
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
            case "myOwn":
                groupMemberPage = groupMemberRepository.findAllByUserAndGroupRole(user, GroupMemberRoleEnum.ADMIN, pageable);
                break;
            case "all":
                groupMemberPage = groupMemberRepository.findAllByUser(user, pageable);
                break;
            default:
                throw new CustomException(ExceptionMessage.INVALID_CATEGORY);
        }

        Page<GroupResponseDto> groupResponseDtoPage = GroupResponseDto.toDtoPage(groupMemberPage);

        //24시간 이내 마감이 있는 그룹일 경우 마감 개수 및 가장 급한것 하나 시간, 제목 노출
        //todo 나중에 쿼리문으로 리팩토링 시도 예정
        for(GroupResponseDto g : groupResponseDtoPage) {
            Group group = groupRepository.findById(g.getGroupId()).get();
            Long deadlineNumber = homeworkRepository.countByExpirationDateBetweenAndGroup
                    (LocalDateTime.now(), LocalDateTime.now().plusDays(1), group);

            if(deadlineNumber > 0) {
                Optional<Homework> deadline = homeworkRepository
                        .findFirstByExpirationDateBetweenAndGroupOrderByExpirationDateAsc
                                (LocalDateTime.now(), LocalDateTime.now().plusDays(1), group);
                deadline.ifPresent(g::addDeadline);
                g.addDeadlineNumber(deadlineNumber);
            }
        }

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
    public ResponseEntity<Message> getGroupMainPage(Long groupId, int page, int size, String category, String sortBy, User user) {
        Group group = groupRepository.findById(groupId).orElseThrow(
                () -> new CustomException(ExceptionMessage.GROUP_NOT_FOUND)
        );

        GroupMember groupMember = groupMemberRepository.findByUserAndGroup(user, group).orElseThrow(
                () -> new CustomException(ExceptionMessage.GROUP_NOT_JOINED)
        );

        //24시간 이내 마감인 과제 리스트
        List<Homework> homeworkList = homeworkRepository.findAllByExpirationDateBetweenAndGroupOrderByExpirationDateAsc(
                LocalDateTime.now(), LocalDateTime.now().plusDays(1), group);


        Pageable pageable = PageRequest.of(page, size);

        Page<BasicBoard> basicBoardPage;

        boolean categoryUsed = category.equals("board") || category.equals("vote")
                || category.equals("homework") || category.equals("notice");
        if(sortBy.equals("createdAt")) {
            if (category.equals("all")) {
                basicBoardPage = basicBoardRepository.findAllByGroupOrderByCreatedAtDesc(group, pageable);
            } else if (categoryUsed) {
                basicBoardPage = basicBoardRepository.findAllByGroupAndDtypeOrderByCreatedAtDesc(group, category, pageable);
            } else {
                throw new CustomException(ExceptionMessage.INVALID_CATEGORY);
            }
        } else if(sortBy.equals("important")) {
            if (category.equals("all")) {
                basicBoardPage = basicBoardRepository.findAllByGroupOrderByImportantDesc(group, pageable);
            } else if (categoryUsed) {
                basicBoardPage = basicBoardRepository.findAllByGroupAndDtypeOrderByImportantDesc(group, category, pageable);
            } else {
                throw new CustomException(ExceptionMessage.INVALID_CATEGORY);
            }
        } else {
            throw new CustomException(ExceptionMessage.INVALID_SORTING);
        }


        //Admin일 경우 관리자 페이지 버튼을 띄우기 위한 Admin 정보
        boolean isAdmin = groupMember.getGroupRole().equals(GroupMemberRoleEnum.ADMIN);

        GroupMainPageResponseDto responseDto = GroupMainPageResponseDto.of(group, isAdmin, homeworkList, basicBoardPage);
        for(MainPageBasicBoardDto m : responseDto.getBasicBoards()) {
            if(m.getDtype().equals("homework")) {
                m.setExpirationDate(homeworkRepository.findById(m.getId()).get().getExpirationDate());
            }
            if(m.getDtype().equals("vote")) {
                m.setExpirationDate(voteRepository.findById(m.getId()).get().getExpirationDate());
            }
        }

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

        //memberId 입력: 관리자의 회원 강퇴 기능
        if(memberId != null) {
            if(!groupMember.getGroupRole().equals(GroupMemberRoleEnum.ADMIN)) {
                throw new CustomException(ExceptionMessage.ADMIN_ONLY);
            }

            User bannedMember = groupMemberRepository.findById(memberId).get().getUser();
            groupMemberRepository.deleteById(memberId);

            //차단목록에 추가
            BannedGroupMember bannedGroupMember = new BannedGroupMember(bannedMember, group);
            bannedGroupMemberRepository.save(bannedGroupMember);

            return Message.toResponseEntity(SuccessMessage.MEMBER_DELETE_SUCCESS);
        }

        //memberId 미입력: 회원의 그룹 탈퇴 기능
        if(groupMember.getGroupRole().equals(GroupMemberRoleEnum.ADMIN)) {
            throw new CustomException(ExceptionMessage.ADMIN_CANNOT_QUIT);
        }

        groupMemberRepository.delete(groupMember);
        return Message.toResponseEntity(SuccessMessage.GROUP_QUIT_SUCCESS);
    }

    @Transactional
    public ResponseEntity<Message> updateGroup(Long groupId, GroupRequestDto requestDto, User user) {
        Group group = groupRepository.findById(groupId).orElseThrow(
                () -> new CustomException(ExceptionMessage.GROUP_NOT_FOUND)
        );

        GroupMember groupMember = groupMemberRepository.findByUserAndGroup(user, group).orElseThrow(
                () -> new CustomException(ExceptionMessage.GROUP_NOT_JOINED)
        );

        if(!groupMember.getGroupRole().equals(GroupMemberRoleEnum.ADMIN)) {
            throw new CustomException(ExceptionMessage.ADMIN_ONLY);
        }

        group.updateGroup(requestDto);

        MultipartFile multipartFile = requestDto.getGroupImage();
        if(!(multipartFile == null || multipartFile.isEmpty())) {
            try {
                if(group.getGroupImage() != null) {
                    String source = URLDecoder.decode(group.getGroupImage().replace("https://s3://project-navis/image/", ""), "UTF-8");
                    s3Uploader.delete(source);
                }

                String groupImage = s3Uploader.upload(multipartFile);
                group.addGroupImage(groupImage);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        GroupDetailsResponseDto responseDto = GroupDetailsResponseDto.of(group);

        return Message.toResponseEntity(SuccessMessage.GROUP_UPDATE_SUCCESS, responseDto);
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

