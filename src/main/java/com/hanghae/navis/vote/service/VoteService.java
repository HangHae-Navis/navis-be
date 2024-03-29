package com.hanghae.navis.vote.service;

import com.hanghae.navis.common.dto.FileResponseDto;
import com.hanghae.navis.common.config.S3Uploader;
import com.hanghae.navis.common.dto.CustomException;
import com.hanghae.navis.common.dto.Message;
import com.hanghae.navis.common.dto.UserGroup;
import com.hanghae.navis.common.entity.File;
import com.hanghae.navis.common.entity.Hashtag;
import com.hanghae.navis.common.repository.FileRepository;
import com.hanghae.navis.common.repository.HashtagRepository;
import com.hanghae.navis.group.dto.RecentlyViewedDto;
import com.hanghae.navis.group.entity.Group;
import com.hanghae.navis.group.entity.GroupMember;
import com.hanghae.navis.group.entity.GroupMemberRoleEnum;
import com.hanghae.navis.group.entity.RecentlyViewed;
import com.hanghae.navis.group.repository.GroupMemberRepository;
import com.hanghae.navis.group.repository.GroupRepository;
import com.hanghae.navis.group.repository.QueryRepository;
import com.hanghae.navis.group.repository.RecentlyViewedRepository;
import com.hanghae.navis.homework.dto.HomeworkRequestDto;
import com.hanghae.navis.homework.dto.HomeworkResponseDto;
import com.hanghae.navis.homework.entity.Homework;
import com.hanghae.navis.notification.entity.NotificationType;
import com.hanghae.navis.notification.service.NotificationService;
import com.hanghae.navis.user.entity.User;
import com.hanghae.navis.user.entity.UserRoleEnum;
import com.hanghae.navis.user.repository.UserRepository;
import com.hanghae.navis.vote.dto.*;
import com.hanghae.navis.vote.entity.Vote;
import com.hanghae.navis.vote.entity.VoteOption;
import com.hanghae.navis.vote.entity.VoteRecord;
import com.hanghae.navis.vote.repository.VoteOptionRepository;
import com.hanghae.navis.vote.repository.VoteRecordRepository;
import com.hanghae.navis.vote.repository.VoteRepository;
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
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.hanghae.navis.common.entity.ExceptionMessage.*;
import static com.hanghae.navis.common.entity.SuccessMessage.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class VoteService {
    private final UserRepository userRepository;
    private final GroupRepository groupRepository;
    private final GroupMemberRepository groupMemberRepository;

    private final VoteRepository voteRepository;
    private final VoteOptionRepository voteOptionRepository;
    private final VoteRecordRepository voteRecordRepository;
    private final FileRepository fileRepository;
    private final HashtagRepository hashtagRepository;
    private final S3Uploader s3Uploader;
    private final RecentlyViewedRepository recentlyViewedRepository;
    private final QueryRepository queryRepository;
    private final NotificationService notificationService;

    @Transactional(readOnly = true)
    public ResponseEntity<Message> getVoteList(Long groupId, User user, int page, int size) {
        //권한 체크
        UserGroup userGroup = authCheck(groupId, user);

        //페이징 처리
        Pageable pageable = PageRequest.of(page, size);
        Page<Vote> votePage = voteRepository.findAllByGroupIdOrderByCreatedAtDesc(groupId, pageable);

        Page<VoteListResponseDto> voteListResponseDto = VoteListResponseDto.toDtoPage(votePage);

        return Message.toResponseEntity(BOARD_LIST_GET_SUCCESS, voteListResponseDto);
    }

    @Transactional
    public ResponseEntity<Message> getVote(Long groupId, Long voteId, User user) {
        //권한체크
        UserGroup userGroup = authCheck(groupId, user);

        //투표 있는지 체크
        Vote vote = voteRepository.findById(voteId).orElseThrow(
                () -> new CustomException(BOARD_NOT_FOUND)
        );

        GroupMember groupMember = groupMemberRepository.findByUserAndGroup(userGroup.getUser(), userGroup.getGroup()).orElseThrow(
                () -> new CustomException(GROUP_NOT_JOINED)
        );

        GroupMemberRoleEnum authorRole = groupMemberRepository.findByUserAndGroup(vote.getUser(), vote.getGroup()).orElseThrow(
                () -> new CustomException(GROUP_NOT_JOINED)
        ).getGroupRole();

        GroupMemberRoleEnum role = groupMember.getGroupRole();

        List<OptionResponseDto> optionResponseDto = new ArrayList<>();


        List<FileResponseDto> fileResponseDto = new ArrayList<>();
        List<String> hashtagList = new ArrayList<>();

        //리턴값으로 보내줄 리스트들 가져옴
        vote.getVoteOptionList().forEach(value -> optionResponseDto.add(OptionResponseDto.of(value)));
        vote.getFileList().forEach(value -> fileResponseDto.add(FileResponseDto.of(value)));
        vote.getHashtagList().forEach(value -> hashtagList.add(value.getHashtagName()));

        Optional<VoteRecord> myPickCheck = voteRecordRepository.findByGroupMemberIdAndVoteId(groupMember.getId(), vote.getId());

        Long myPick = -1L;

        if (myPickCheck.isPresent()) {
            myPick = myPickCheck.get().getVoteOption().getId();
        }

        RecentlyViewed recentlyViewed = new RecentlyViewed(groupMember, vote);
        recentlyViewedRepository.save(recentlyViewed);

        List<RecentlyViewedDto> rv = queryRepository.findRecentlyViewedsByGroupMemeber(groupMember.getId());

        boolean author = user.equals(vote.getUser());


        if (!role.name().equals("USER")) {
            List<OptionAdminResponseDto> optionAdminResponseDto = new ArrayList<>();
            for (VoteOption voteOption : vote.getVoteOptionList()) {
                List<PickUserInfoDto> pickUserInfoDtoList = new ArrayList<>();
                List<VoteRecord> voteRecordList = voteOption.getVoteRecordList();
                for (VoteRecord voteRecord : voteRecordList) {
                    pickUserInfoDtoList.add(PickUserInfoDto.of(voteRecord.getGroupMember().getUser(), voteRecord.getVoteOption().getId()));
                }
                optionAdminResponseDto.add(OptionAdminResponseDto.of(voteOption, pickUserInfoDtoList));
            }

            VoteAdminResponseDto voteAdminResponseDto = VoteAdminResponseDto.of(vote, fileResponseDto, hashtagList, optionAdminResponseDto, expirationCheck(
                    vote.getExpirationDate()), vote.getExpirationDate(), role, myPick, rv, authorRole, author);

            return Message.toResponseEntity(BOARD_DETAIL_GET_SUCCESS, voteAdminResponseDto);
        }

        VoteResponseDto voteResponseDto = VoteResponseDto.of(vote, fileResponseDto, hashtagList, optionResponseDto, expirationCheck(
                vote.getExpirationDate()), vote.getExpirationDate(), role, myPick, rv, authorRole, author);

        return Message.toResponseEntity(BOARD_DETAIL_GET_SUCCESS, voteResponseDto);
    }


    @Transactional
    public ResponseEntity<Message> createVote(Long groupId, VoteRequestDto requestDto, User user) {
        try {
            //유저의 권한을 체크
            UserGroup userGroup = authCheck(groupId, user);

            GroupMember groupMember = groupMemberRepository.findByUserAndGroup(userGroup.getUser(), userGroup.getGroup()).orElseThrow(
                    () -> new CustomException(GROUP_NOT_JOINED)
            );

            GroupMemberRoleEnum role = groupMember.getGroupRole();

            if(role.equals(GroupMemberRoleEnum.USER)) {
                return Message.toExceptionResponseEntity(ADMIN_ONLY);
            }

            //권한이 있으면 투표를 생성
            Vote vote = new Vote(requestDto, userGroup.getUser(), userGroup.getGroup(), unixTimeToLocalDateTime(requestDto.getExpirationDate()), false);
            voteRepository.saveAndFlush(vote);

            List<String> hashtagList = new ArrayList<>();

            //받아온 해시태그를 띄어쓰기로 구분 후 처리
            for (String tag : requestDto.getHashtagList().split(" ")) {
                if (tag.length() > 10) {
                    throw new CustomException(HASHTAG_LENGTH_ERROR);
                }
                Hashtag hashtag = new Hashtag(tag, vote);
                hashtagRepository.save(hashtag);
                hashtagList.add(tag);
            }

            List<FileResponseDto> fileResponseDto = new ArrayList<>();
            List<MultipartFile> multipartFiles = requestDto.getMultipartFiles();
            if (multipartFiles != null) {
                //다중파일을 처리
                for (MultipartFile file : multipartFiles) {
                    String fileTitle = file.getOriginalFilename();
                    String fileUrl = s3Uploader.upload(file);
                    File voteFile = new File(fileTitle, fileUrl, vote);
                    fileRepository.save(voteFile);
                    fileResponseDto.add(FileResponseDto.of(voteFile));
                }
            }
            List<OptionResponseDto> optionResponseDto = new ArrayList<>();

            //다중 투표선택지 처리
            for (String option : requestDto.getOptionList().split(" ")) {
                if(option.length() > 30){
                    throw new CustomException(OPTION_LENGTH_ERROR);
                }
                VoteOption voteOption = new VoteOption(vote, option);
                voteOptionRepository.save(voteOption);
                optionResponseDto.add(OptionResponseDto.of(voteOption));
            }

            RecentlyViewed recentlyViewed = new RecentlyViewed(groupMember, vote);
            recentlyViewedRepository.save(recentlyViewed);

            List<RecentlyViewedDto> rv = queryRepository.findRecentlyViewedsByGroupMemeber(groupMember.getId());

            //리턴으로 보내줄 dto생성
            VoteResponseDto voteResponseDto = VoteResponseDto.of(vote, fileResponseDto, hashtagList,
                    optionResponseDto, false, unixTimeToLocalDateTime(requestDto.getExpirationDate()), role,
                    null, rv, groupMember.getGroupRole(), true);

            notificationService.send(userGroup.getUser(), NotificationType.VOTE_POST, userGroup.getGroup().getGroupName() + "에서 " + NotificationType.VOTE_POST.getContent(), "http://navis.kro.kr/party/detail?groupId=" + groupId + "&detailId=" + vote.getId() + "&dtype=vote", userGroup.getGroup());

            return Message.toResponseEntity(BOARD_POST_SUCCESS, voteResponseDto);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
//
//    @Transactional
//    public ResponseEntity<Message> updateVote(Long groupId, Long voteId, VoteRequestDto requestDto, User user) {
//        UserGroup userGroup = authCheck(groupId, user);
//
//        Vote vote = voteRepository.findById(voteId).orElseThrow(
//                () -> new CustomException(BOARD_NOT_FOUND)
//        );
//
//
//        if (!user.getUsername().equals(vote.getUser().getUsername())) {
//            throw new CustomException(UNAUTHORIZED_UPDATE_OR_DELETE);
//        }
//
//        GroupMember groupMember = groupMemberRepository.findByUserAndGroup(userGroup.getUser(), userGroup.getGroup()).orElseThrow(
//                () -> new CustomException(GROUP_NOT_JOINED)
//        );
//
//        GroupMemberRoleEnum role = groupMember.getGroupRole();
//
//        GroupMemberRoleEnum authorRole = groupMemberRepository.findByUserAndGroup(vote.getUser(), vote.getGroup()).orElseThrow(
//                () -> new CustomException(GROUP_NOT_JOINED)
//        ).getGroupRole();
//
//        vote.update(requestDto, unixTimeToLocalDateTime(requestDto.getExpirationDate()), expirationCheck(vote.getExpirationDate()));
//
//        if (!groupMember.getGroupRole().equals(GroupMemberRoleEnum.ADMIN) && !groupMember.getGroupRole().equals(GroupMemberRoleEnum.SUPPORT)) {
//            throw new CustomException(ADMIN_ONLY);
//        }
//
//        boolean author = user.equals(vote.getUser());
//
//        List<RecentlyViewedDto> rv = queryRepository.findRecentlyViewedsByGroupMemeber(groupMember.getId());
//
//        VoteResponseDto voteResponseDto = VoteResponseDto.of(vote, null, null,
//                null, false, unixTimeToLocalDateTime(requestDto.getExpirationDate()), role,
//                null, rv, groupMember.getGroupRole(), true);
//
//
//
//
//
//
//
//        vote.update(requestDto, unixTimeToLocalDateTime(requestDto.getExpirationDate()), expirationCheck(vote.getExpirationDate()));
//
//        List<Hashtag> remainTag = hashtagRepository.findAllByBasicBoardId(voteId);
//
//        for (Hashtag hashtag : remainTag) {
//            vote.getFileList().remove(hashtag);
//            hashtagRepository.delete(hashtag);
//        }
//
//        List<String> hashtagResponseDto = new ArrayList<>();
//
//        for (String tag : requestDto.getHashtagList().split(" ")) {
//            if (tag.length() > 10) {
//                throw new CustomException(HASHTAG_LENGTH_ERROR);
//            }
//            Hashtag hashtag = new Hashtag(tag, vote);
//            hashtagRepository.save(hashtag);
//            hashtagResponseDto.add(tag);
//        }
//
//        List<MultipartFile> multipartFiles = requestDto.getMultipartFiles();
//        List<File> files = fileRepository.findFileUrlByBasicBoardId(voteId);
//        List<OptionResponseDto> optionResponseDto = new ArrayList<>();
//
//        //다중 투표선택지 처리
//        try {
//            for (File boardFile : files) {
//                if (!multipartFiles.contains(boardFile.getFileUrl())) {
//                    vote.getFileList().remove(boardFile);
//                    String source = URLDecoder.decode(boardFile.getFileUrl().replace("https://s3://project-navis/image/", ""), "UTF-8");
//                    s3Uploader.delete(source);
//                    fileRepository.delete(boardFile);
//                }
//            }
//            for (VoteOption option : vote.getVoteOptionList()) {
//                if (!remainOption.contains(new OptionRequestDto(option.getOption()))) {
//                    vote.getVoteOptionList().remove(option);
//                    voteOptionRepository.delete(option);
//                }
//            }
//        } catch (UnsupportedEncodingException e) {
//            throw new RuntimeException(e);
//        }
//
//        List<OptionResponseDto> optionResponseDto = new ArrayList<>();
//        for (VoteOption voteOption : vote.getVoteOptionList()) {
//            optionResponseDto.add(new OptionResponseDto(voteOption.getOption(), (long) voteOption.getVoteRecordList().size()));
//        }
//
//
//        List<FileResponseDto> fileResponseDto = new ArrayList<>();
//        try {
//            if (multipartFiles != null) {
//                if (!multipartFiles.isEmpty() && !multipartFiles.get(0).isEmpty()) {
//                    for (MultipartFile file : multipartFiles) {
//                        String fileTitle = file.getOriginalFilename();
//                        String fileUrl = s3Uploader.upload(file);
//                        File votefile = new File(fileTitle, fileUrl, vote);
//                        fileRepository.save(votefile);
//                        fileResponseDto.add(new FileResponseDto(votefile.getFileTitle(), votefile.getFileUrl()));
//                    }
//                }
//            }
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//
//        VoteResponseDto voteResponseDto = new VoteResponseDto(vote, fileResponseDto, optionResponseDto, expirationCheck(vote.getExpirationDate()), vote.getExpirationDate());
//
//        return Message.toResponseEntity(BOARD_PUT_SUCCESS, voteResponseDto);
//    }

    @Transactional
    public ResponseEntity<Message> deleteVote(Long groupId, Long voteId, User user) {
        //유저의 권한을 체크
        UserGroup userGroup = authCheck(groupId, user);

        //투표가 제대로 있는지 확인
        Vote vote = voteRepository.findById(voteId).orElseThrow(
                () -> new CustomException(BOARD_NOT_FOUND)
        );

        GroupMemberRoleEnum myRole = groupMemberRepository.findByUserAndGroup(userGroup.getUser(), userGroup.getGroup()).orElseThrow(
                () -> new CustomException(GROUP_NOT_JOINED)
        ).getGroupRole();

        GroupMemberRoleEnum authorRole = groupMemberRepository.findByUserAndGroup(vote.getUser(), vote.getGroup()).orElseThrow(
                () -> new CustomException(GROUP_NOT_JOINED)
        ).getGroupRole();

        if(!authorRole.equals(GroupMemberRoleEnum.ADMIN)){
            if(!vote.getUser().equals(userGroup.getUser())){
                return Message.toExceptionResponseEntity(UNAUTHORIZED_UPDATE_OR_DELETE);
            }
        }

        //투표에 파일이 있다면 s3에서 삭제
        if (vote.getFileList().size() > 0) {
            try {
                for (File file : vote.getFileList()) {
                    String source = URLDecoder.decode(file.getFileUrl().replace("https://s3://project-navis/image/", ""), "UTF-8");
                    s3Uploader.delete(source);
                }
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
        }

        voteRepository.deleteById(voteId);

        return Message.toResponseEntity(BOARD_DELETE_SUCCESS);
    }


    @Transactional
    public ResponseEntity<Message> unPickVote(Long groupId, Long voteId, User user) {
        //유저의 권한을 체크
        UserGroup userGroup = authCheck(groupId, user);

        //투표가 제대로 있는지 확인
        Vote vote = voteRepository.findById(voteId).orElseThrow(
                () -> new CustomException(BOARD_NOT_FOUND)
        );

        //그룹멤버 체크
        GroupMember groupMember = groupMemberRepository.findByUserAndGroup(userGroup.getUser(), userGroup.getGroup()).orElseThrow(
                () -> new CustomException(GROUP_MEMBER_NOT_FOUND)
        );

        voteRecordRepository.deleteByGroupMemberIdAndVoteId(groupMember.getId(), vote.getId());

        return Message.toResponseEntity(VOTE_CANCEL_SUCCESS);
    }


    @Transactional
    public ResponseEntity<Message> forceExpired(Long groupId, Long voteId, User user) {
        //유저의 권한을 체크
        UserGroup userGroup = authCheck(groupId, user);

        //투표가 제대로 있는지 확인
        Vote vote = voteRepository.findById(voteId).orElseThrow(
                () -> new CustomException(BOARD_NOT_FOUND)
        );

        GroupMember groupMember = groupMemberRepository.findByUserAndGroup(userGroup.getUser(), userGroup.getGroup()).orElseThrow(
                () -> new CustomException(GROUP_NOT_JOINED)
        );

        GroupMemberRoleEnum role = groupMember.getGroupRole();


        //투표를 강제로 만료시킴
        if (role != GroupMemberRoleEnum.USER) {
            vote.forceExpiration();
        } else {
            throw new CustomException(ADMIN_ONLY);
        }

        return Message.toResponseEntity(VOTE_FORCE_EXPIRED_SUCCESS);
    }

    @Transactional
    public ResponseEntity<Message> pickVote(Long groupId, Long voteId, Long voteOptionId, User user) {
        //유저의 권한을 체크
        UserGroup userGroup = authCheck(groupId, user);

        //투표 체크
        Vote vote = voteRepository.findById(voteId).orElseThrow(
                () -> new CustomException(BOARD_NOT_FOUND)
        );

        //투표 선택지 체크
        VoteOption voteOption = voteOptionRepository.findById(voteOptionId).orElseThrow(
                () -> new CustomException(VOTE_OPTION_NOT_FOUND)
        );

        //그룹멤버 체크
        GroupMember groupMember = groupMemberRepository.findByUserAndGroup(userGroup.getUser(), userGroup.getGroup()).orElseThrow(
                () -> new CustomException(GROUP_MEMBER_NOT_FOUND)
        );

        GroupMemberRoleEnum authorRole = groupMemberRepository.findByUserAndGroup(vote.getUser(), vote.getGroup()).orElseThrow(
                () -> new CustomException(GROUP_NOT_JOINED)
        ).getGroupRole();

        boolean author = user.equals(vote.getUser());

        GroupMemberRoleEnum role = groupMember.getGroupRole();

        List<RecentlyViewedDto> rv = queryRepository.findRecentlyViewedsByGroupMemeber(groupMember.getId());

        //투표시간 / 강제만료 체크 후 만료가 아니면 투표 가능하게설정
        if (!(expirationCheck(vote.getExpirationDate()) || vote.isForceExpiration())) {
            if (!voteRecordRepository.findByGroupMemberIdAndVoteOptionId(groupMember.getId(), voteOption.getId()).isPresent()) {
                VoteRecord voteRecord = new VoteRecord(vote, voteOption, groupMember);
                voteRecordRepository.save(voteRecord);
                return Message.toResponseEntity(VOTE_PICK_SUCCESS,
                        VoteResponseDto.of(vote,
                                parseFileResponseDto(vote.getFileList()),
                                null,
                                parseOptionResponseDto(vote.getVoteOptionList()),
                                vote.isForceExpiration(),
                                vote.getExpirationDate(), role, -1L, rv, authorRole, author));
            }
        }
        return Message.toExceptionResponseEntity(VOTE_EXPIRED);
    }

    @Transactional
    public ResponseEntity<Message> deleteHashtag(Long groupId, Long hashtagId, User user) {
        //유저의 권한을 체크
        UserGroup userGroup = authCheck(groupId, user);

        //해시태그 체크
        Hashtag hashtag = hashtagRepository.findById(hashtagId).orElseThrow(
                () -> new CustomException(HASHTAG_NOT_FOUND)
        );

        hashtagRepository.deleteById(hashtagId);
        return Message.toResponseEntity(HASHTAG_DELETE_SUCCESS);
    }

    public UserGroup authCheck(Long groupId, User user) {

        Group group = groupRepository.findById(groupId).orElseThrow(
                () -> new CustomException(GROUP_NOT_FOUND)
        );

        User me = userRepository.findByUsername(user.getUsername()).orElseThrow(
                () -> new CustomException(MEMBER_NOT_FOUND)
        );

        if (!groupMemberRepository.findByUserAndGroup(me, group).isPresent()) {
            throw new CustomException(GROUP_MEMBER_NOT_FOUND);
        }

        return new UserGroup(me, group);
    }

    public List<FileResponseDto> parseFileResponseDto(List<File> fileList) {
        List<FileResponseDto> fileResponseDto = new ArrayList<>();
        fileList.forEach(value -> fileResponseDto.add(FileResponseDto.of(value)));
        return fileResponseDto;
    }

    public List<OptionResponseDto> parseOptionResponseDto(List<VoteOption> voteOptionList) {
        List<OptionResponseDto> optionResponseDto = new ArrayList<>();

        voteOptionList.forEach(value -> optionResponseDto.add(OptionResponseDto.of(value)));

        return optionResponseDto;
    }

    public LocalDateTime unixTimeToLocalDateTime(Long unixTime) {
        return LocalDateTime.ofEpochSecond(unixTime, 6, ZoneId.of("Asia/Seoul").getRules().getOffset(Instant.now()));
    }

    public boolean expirationCheck(LocalDateTime dbTime) {
        return LocalDateTime.now().isAfter(dbTime);
    }


}
