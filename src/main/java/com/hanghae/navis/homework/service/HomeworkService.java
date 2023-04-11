package com.hanghae.navis.homework.service;

import com.hanghae.navis.common.config.S3Service;
import com.hanghae.navis.common.config.S3Uploader;
import com.hanghae.navis.common.dto.*;
import com.hanghae.navis.common.entity.File;
import com.hanghae.navis.common.entity.Hashtag;
import com.hanghae.navis.common.entity.SuccessMessage;
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
import com.hanghae.navis.homework.dto.*;
import com.hanghae.navis.homework.entity.Feedback;
import com.hanghae.navis.homework.entity.Homework;
import com.hanghae.navis.homework.entity.HomeworkSubject;
import com.hanghae.navis.homework.entity.HomeworkSubjectFile;
import com.hanghae.navis.homework.repository.*;
import com.hanghae.navis.notification.entity.Notification;
import com.hanghae.navis.notification.entity.NotificationType;
import com.hanghae.navis.notification.service.NotificationService;
import com.hanghae.navis.user.entity.User;
import com.hanghae.navis.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
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
import java.util.*;

import static com.hanghae.navis.common.entity.ExceptionMessage.*;
import static com.hanghae.navis.common.entity.SuccessMessage.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class HomeworkService {
    private final HomeworkSubjectFileRepository homeworkSubjectFileRepository;
    private final HomeworkSubjectRepository homeworkSubjectRepository;
    private final HomeworkRepository homeworkRepository;
    private final FileRepository fileRepository;
    private final UserRepository userRepository;
    private final GroupRepository groupRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final HashtagRepository hashtagRepository;
    private final SubmitRepository submitRepository;
    private final FeedbackRepository feedbackRepository;
    private final S3Uploader s3Uploader;
    private final S3Service s3Service;
    private final NotificationService notificationService;
    private final RecentlyViewedRepository recentlyViewedRepository;
    private final QueryRepository queryRepository;

    @Transactional(readOnly = true)
    public ResponseEntity<Message> homeworkList(Long groupId, int page, int size, User user) {
        Group group = groupRepository.findById(groupId).orElseThrow(
                () -> new CustomException(GROUP_NOT_FOUND)
        );

        user = userRepository.findByUsername(user.getUsername()).orElseThrow(
                () -> new CustomException(MEMBER_NOT_FOUND)
        );

        Pageable pageable = PageRequest.of(page, size);

        Page<Homework> homeworkList = homeworkRepository.findAllByGroupIdOrderByCreatedAtDesc(groupId, pageable);

        Page<HomeworkListResponseDto> homeworkListResponseDto = HomeworkListResponseDto.toDto(homeworkList);

        return Message.toResponseEntity(BOARD_LIST_GET_SUCCESS, homeworkListResponseDto);
    }

    @Transactional
    public ResponseEntity<Message> getHomework(Long groupId, Long boardId, User user) {
        Group group = groupRepository.findById(groupId).orElseThrow(
                () -> new CustomException(GROUP_NOT_FOUND)
        );

        user = userRepository.findByUsername(user.getUsername()).orElseThrow(
                () -> new CustomException(MEMBER_NOT_FOUND)
        );

        Homework homework = homeworkRepository.findById(boardId).orElseThrow(
                () -> new CustomException(BOARD_NOT_FOUND)
        );

        GroupMember groupMember = groupMemberRepository.findByUserAndGroup(user, group).orElseThrow(
                () -> new CustomException(GROUP_NOT_JOINED)
        );

        GroupMemberRoleEnum role = groupMember.getGroupRole();

        List<String> hashtagResponseDto = new ArrayList<>();
        homework.getHashtagList().forEach(value -> hashtagResponseDto.add(value.getHashtagName()));

        List<RecentlyViewedDto> rv = queryRepository.findRecentlyViewedsByGroupMemeber(groupMember.getId());

        //admin, support return
        if (role.equals(GroupMemberRoleEnum.ADMIN) || role.equals(GroupMemberRoleEnum.SUPPORT)) {
            List<FileResponseDto> fileResponseDto = new ArrayList<>();

            homework.getFileList().forEach(value -> fileResponseDto.add(FileResponseDto.of(value)));

            List<HomeworkSubmitListResponseDto> memberList = submitRepository.findByGroupMember(groupId, boardId);

            List<NotSubmitMemberResponseDto> notSubmit = new ArrayList<>();
            List<SubmitMemberResponseDto> submitMember = new ArrayList<>();

            for (HomeworkSubmitListResponseDto member : memberList) {
                if (member.getRole().equals("USER")) {
                    if (member.getSubmit() != null) {
                        submitMember.add(SubmitMemberResponseDto.of(member));
                    } else {
                        notSubmit.add(NotSubmitMemberResponseDto.of(member));
                    }
                }
            }

            AdminHomeworkResponseDto adminResponse = AdminHomeworkResponseDto.of(homework, hashtagResponseDto, fileResponseDto, notSubmit, submitMember, role, rv);

            RecentlyViewed recentlyViewed = new RecentlyViewed(groupMember, homework);
            recentlyViewedRepository.save(recentlyViewed);
            return Message.toResponseEntity(BOARD_DETAIL_GET_SUCCESS, adminResponse);
        }

        //user return
        //게시글 파일리스트
        List<FileResponseDto> responseList = new ArrayList<>();
        homework.getFileList().forEach(value -> responseList.add(FileResponseDto.of(value)));

        //제출한 과제 파일리스트
        List<HomeworkFileResponseDto> fileResponseDto = new ArrayList<>();

        //피드백 리스트
        List<String> feedbackResponse = new ArrayList<>();

        HomeworkSubject homeworkSubject = homeworkSubjectRepository.findByUserIdAndGroupIdAndHomeworkId(user.getId(), groupId, homework.getId());


        if (homeworkSubject == null) { //미제출 유저
            HomeworkResponseDto homeworkResponseDto = HomeworkResponseDto.of(homework, responseList, hashtagResponseDto, expirationCheck(homework.getExpirationDate()), homework.getExpirationDate(), role, rv);

            RecentlyViewed recentlyViewed = new RecentlyViewed(groupMember, homework);
            recentlyViewedRepository.save(recentlyViewed);
            return Message.toResponseEntity(BOARD_DETAIL_GET_SUCCESS, homeworkResponseDto);
        } else {    //제출한 유저
            homeworkSubject.getHomeworkSubjectFileList().forEach(value -> fileResponseDto.add(HomeworkFileResponseDto.of(value)));
            homeworkSubject.getFeedbackList().forEach(value -> feedbackResponse.add(value.getFeedback()));
            SubmitResponseDto submitResponseDto = SubmitResponseDto.of(homeworkSubject, fileResponseDto, feedbackResponse);
            HomeworkResponseDto homeworkResponseDto = HomeworkResponseDto.of(homework, responseList, hashtagResponseDto, expirationCheck(homework.getExpirationDate()), homework.getExpirationDate(), role, submitResponseDto, rv);

            RecentlyViewed recentlyViewed = new RecentlyViewed(groupMember, homework);
            recentlyViewedRepository.save(recentlyViewed);
            return Message.toResponseEntity(BOARD_DETAIL_GET_SUCCESS, homeworkResponseDto);
        }
    }

    @Transactional
    public ResponseEntity<Message> createHomework(Long groupId, HomeworkRequestDto requestDto, User user) {
        Group group = groupRepository.findById(groupId).orElseThrow(
                () -> new CustomException(GROUP_NOT_FOUND)
        );

        user = userRepository.findByUsername(user.getUsername()).orElseThrow(
                () -> new CustomException(MEMBER_NOT_FOUND)
        );

        GroupMember groupMember = groupMemberRepository.findByUserAndGroup(user, group).orElseThrow(
                () -> new CustomException(GROUP_NOT_JOINED)
        );

        GroupMemberRoleEnum role = groupMember.getGroupRole();

        if (!groupMember.getGroupRole().equals(GroupMemberRoleEnum.ADMIN) && !groupMember.getGroupRole().equals(GroupMemberRoleEnum.SUPPORT)) {
            throw new CustomException(ADMIN_ONLY);
        }

        Homework homework = new Homework(requestDto, user, group, unixTimeToLocalDateTime(requestDto.getExpirationDate()), expirationCheck(unixTimeToLocalDateTime(requestDto.getExpirationDate())));

        homeworkRepository.save(homework);

        List<String> hashtagList = new ArrayList<>();

        for (String tag : requestDto.getHashtagList().split(" ")) {
            Hashtag hashtag = new Hashtag(tag, homework);
            hashtagRepository.save(hashtag);
            hashtagList.add(tag);
        }

        List<FileResponseDto> fileResponseDto = new ArrayList<>();

        try {
            if (requestDto.getMultipartFiles() != null) {
                for (MultipartFile file : requestDto.getMultipartFiles()) {
                    String fileTitle = file.getOriginalFilename();
                    String fileUrl = s3Uploader.upload(file);
                    File homeworkFile = new File(fileTitle, fileUrl, homework);
                    fileRepository.save(homeworkFile);
                    fileResponseDto.add(FileResponseDto.of(homeworkFile));
                }
            } else {
                fileResponseDto = null;
            }

            List<RecentlyViewedDto> rv = queryRepository.findRecentlyViewedsByGroupMemeber(groupMember.getId());

            HomeworkResponseDto responseDto = HomeworkResponseDto.of(homework, fileResponseDto, hashtagList, expirationCheck(unixTimeToLocalDateTime(requestDto.getExpirationDate())), unixTimeToLocalDateTime(requestDto.getExpirationDate()), role, rv);

            List<GroupMember> groupMemberList = groupMemberRepository.findAllByGroupId(groupId);


            notificationService.send(user, NotificationType.HOMEWORK_POST,  group.getGroupName() + "에서 " + NotificationType.HOMEWORK_POST.getContent(), "http://navis.kro.kr/party/detail?groupId=" + groupId + "&detailId=" + homework.getId() + "&dtype=homework", group);
            RecentlyViewed recentlyViewed = new RecentlyViewed(groupMember, homework);
            recentlyViewedRepository.save(recentlyViewed);
            return Message.toResponseEntity(SuccessMessage.BOARD_POST_SUCCESS, responseDto);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public ResponseEntity<Message> updateHomework(Long groupId, Long boardId, HomeworkRequestDto requestDto, User user) {
        Group group = groupRepository.findById(groupId).orElseThrow(
                () -> new CustomException(GROUP_NOT_FOUND)
        );

        user = userRepository.findByUsername(user.getUsername()).orElseThrow(
                () -> new CustomException(MEMBER_NOT_FOUND)
        );

        Homework homework = homeworkRepository.findById(boardId).orElseThrow(
                () -> new CustomException(BOARD_NOT_FOUND)
        );

        GroupMember groupMember = groupMemberRepository.findByUserAndGroup(user, group).orElseThrow(
                () -> new CustomException(GROUP_NOT_JOINED)
        );

        GroupMemberRoleEnum role = groupMember.getGroupRole();

        if (!groupMember.getGroupRole().equals(GroupMemberRoleEnum.ADMIN) && !groupMember.getGroupRole().equals(GroupMemberRoleEnum.SUPPORT)) {
            throw new CustomException(ADMIN_ONLY);
        }

        List<RecentlyViewedDto> rv = queryRepository.findRecentlyViewedsByGroupMemeber(groupMember.getId());        HomeworkResponseDto responseDto = HomeworkResponseDto.of(homework, null, null, expirationCheck(homework.getExpirationDate()), homework.getExpirationDate(), role, rv);

        homework.update(requestDto, unixTimeToLocalDateTime(requestDto.getExpirationDate()), expirationCheck(homework.getExpirationDate()));

        List<Hashtag> remainTag = hashtagRepository.findAllByBasicBoardId(boardId);

        for (Hashtag hashtag : remainTag) {
            homework.getFileList().remove(hashtag);
            hashtagRepository.delete(hashtag);
        }

        List<String> hashtagResponseDto = new ArrayList<>();

        for (String tag : requestDto.getHashtagList().split(" ")) {
            Hashtag hashtag = new Hashtag(tag, homework);
            hashtagRepository.save(hashtag);
            hashtagResponseDto.add(tag);
        }

        List<MultipartFile> multipartFiles = requestDto.getMultipartFiles();
        List<File> files = fileRepository.findFileUrlByBasicBoardId(boardId);

        try {
            for (File boardFile : files) {
                if (!multipartFiles.contains(boardFile.getFileUrl())) {
                    homework.getFileList().remove(boardFile);
                    String source = URLDecoder.decode(boardFile.getFileUrl().replace("https://s3://project-navis/image/", ""), "UTF-8");
                    s3Uploader.delete(source);
                    fileRepository.delete(boardFile);
                }
            }
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

        try {
            List<FileResponseDto> fileResponseDto = new ArrayList<>();
            if (requestDto.getMultipartFiles() != null) {
                for (MultipartFile file : requestDto.getMultipartFiles()) {
                    String fileTitle = file.getOriginalFilename();
                    String fileUrl = s3Uploader.upload(file);
                    File homeworkFile = new File(fileTitle, fileUrl, homework);
                    fileRepository.save(homeworkFile);
                    fileResponseDto.add(FileResponseDto.of(homeworkFile));
                }
            } else {
                fileResponseDto = null;
            }
            responseDto = HomeworkResponseDto.of(homework, fileResponseDto, hashtagResponseDto, expirationCheck(homework.getExpirationDate()), homework.getExpirationDate(), role, rv);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return Message.toResponseEntity(BOARD_PUT_SUCCESS, responseDto);
    }

    @Transactional
    public ResponseEntity<Message> deleteHomework(Long groupId, Long boardId, User user) {
        Group group = groupRepository.findById(groupId).orElseThrow(
                () -> new CustomException(GROUP_NOT_FOUND)
        );

        user = userRepository.findByUsername(user.getUsername()).orElseThrow(
                () -> new CustomException(MEMBER_NOT_FOUND)
        );

        Homework homework = homeworkRepository.findById(boardId).orElseThrow(
                () -> new CustomException(BOARD_NOT_FOUND)
        );

        GroupMember groupMember = groupMemberRepository.findByUserAndGroup(user, group).orElseThrow(
                () -> new CustomException(GROUP_NOT_JOINED)
        );

        GroupMemberRoleEnum role = groupMember.getGroupRole();

        if (!groupMember.getGroupRole().equals(GroupMemberRoleEnum.ADMIN) && !groupMember.getGroupRole().equals(GroupMemberRoleEnum.SUPPORT)) {
            throw new CustomException(ADMIN_ONLY);
        }

        if (homework.getFileList().size() > 0) {
            try {
                for (File file : homework.getFileList()) {
                    String source = URLDecoder.decode(file.getFileUrl().replace("https://s3://project-navis/image/", ""), "UTF-8");
                    s3Uploader.delete(source);
                }
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
        }
        homeworkRepository.deleteById(boardId);
        return Message.toResponseEntity(BOARD_DELETE_SUCCESS);
    }

    @Transactional
    public ResponseEntity<Message> submitHomework(Long groupId, Long boardId, HomeworkFileRequestDto requestDto, User user) {
        try {
            Group group = groupRepository.findById(groupId).orElseThrow(
                    () -> new CustomException(GROUP_NOT_FOUND)
            );

            user = userRepository.findByUsername(user.getUsername()).orElseThrow(
                    () -> new CustomException(MEMBER_NOT_FOUND)
            );

            Homework homework = homeworkRepository.findById(boardId).orElseThrow(
                    () -> new CustomException(BOARD_NOT_FOUND)
            );

            GroupMember groupMember = groupMemberRepository.findByUserAndGroup(user, group).orElseThrow(
                    () -> new CustomException(GROUP_NOT_JOINED)
            );

            HomeworkSubject homeworkSubject = homeworkSubjectRepository.findByUserIdAndGroupIdAndHomeworkId(user.getId(), groupId, homework.getId());

            if (homeworkSubject != null) {
                throw new CustomException(DUPLICATE_HOMEWORK);
            }

            HomeworkSubject subject = new HomeworkSubject();

            if (expirationCheck(homework.getExpirationDate()) == true) {
                subject = new HomeworkSubject(true, true, false, user, group, homework);
            } else {
                subject = new HomeworkSubject(true, false, false, user, group, homework);
            }
            homeworkSubjectRepository.save(subject);

            List<HomeworkFileResponseDto> fileResponseDto = new ArrayList<>();

            if (requestDto.getMultipartFiles() != null) {
                for (MultipartFile file : requestDto.getMultipartFiles()) {
                    String fileName = file.getOriginalFilename();
                    String fileUrl = s3Uploader.upload(file);
                    HomeworkSubjectFile subjectFile = new HomeworkSubjectFile(fileUrl, fileName, subject);
                    homeworkSubjectFileRepository.save(subjectFile);
                    fileResponseDto.add(HomeworkFileResponseDto.of(subjectFile));
                }

                SubmitResponseDto submitResponseDto = SubmitResponseDto.of(subject, fileResponseDto, null);
                return Message.toResponseEntity(HOMEWORK_SUBMIT_SUCCESS, submitResponseDto);
            } else {
                throw new CustomException(HOMEWORK_FILE_IS_NULL);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public ResponseEntity<Message> updateHomeworkSubject(Long groupId, Long boardId, HomeworkFileRequestDto requestDto, User user) {
        try {
            Group group = groupRepository.findById(groupId).orElseThrow(
                    () -> new CustomException(GROUP_NOT_FOUND)
            );

            Homework homework = homeworkRepository.findById(boardId).orElseThrow(
                    () -> new CustomException(BOARD_NOT_FOUND)
            );

            user = userRepository.findByUsername(user.getUsername()).orElseThrow(
                    () -> new CustomException(MEMBER_NOT_FOUND)
            );

            GroupMember groupMember = groupMemberRepository.findByUserAndGroup(user, group).orElseThrow(
                    () -> new CustomException(MEMBER_NOT_FOUND)
            );

            HomeworkSubject subject = homeworkSubjectRepository.findByUserIdAndGroupIdAndHomeworkId(user.getId(), groupId, homework.getId());

            if (!user.getId().equals(subject.getUser().getId())) {
                throw new CustomException(UNAUTHORIZED_UPDATE_OR_DELETE);
            }

            if (subject.isSubmitCheck() == true) {
                throw new CustomException(HOMEWORK_SUBMIT_CHECK_TRUE);
            }

            List<HomeworkSubjectFile> subjectFileList = homeworkSubjectFileRepository.findFileUrlByHomeworkSubjectId(subject.getId());

            List<MultipartFile> multipartFiles = requestDto.getMultipartFiles();

            try {
                for (HomeworkSubjectFile file : subjectFileList) {
                    if (!multipartFiles.contains(file.getFileUrl())) {
                        subject.getHomeworkSubjectFileList().remove(file);
                        String source = URLDecoder.decode(file.getFileUrl().replace("https://s3://project-navis/image/", ""), "UTF-8");
                        s3Uploader.delete(source);
                        homeworkSubjectFileRepository.delete(file);
                    }
                }
            } catch (UnsupportedEncodingException e) {
                throw new CustomException(HOMEWORK_FILE_IS_NULL);
            }

            for (MultipartFile saveFile : multipartFiles) {
                String fileName = saveFile.getOriginalFilename();
                String fileUrl = s3Uploader.upload(saveFile);

                HomeworkSubjectFile subjectFile = new HomeworkSubjectFile(fileUrl, fileName, subject);
                homeworkSubjectFileRepository.save(subjectFile);
                subject.updateSubject(true);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return Message.toResponseEntity(HOMEWORK_SUBJECT_FILE_UPDATE_SUCCESS);
    }

    @Transactional
    public ResponseEntity<Message> submitCancel(Long groupId, Long boardId, User user) {
        Group group = groupRepository.findById(groupId).orElseThrow(
                () -> new CustomException(GROUP_NOT_FOUND)
        );

        user = userRepository.findByUsername(user.getUsername()).orElseThrow(
                () -> new CustomException(MEMBER_NOT_FOUND)
        );

        Homework homework = homeworkRepository.findById(boardId).orElseThrow(
                () -> new CustomException(BOARD_NOT_FOUND)
        );

        GroupMember groupMember = groupMemberRepository.findByUserAndGroup(user, group).orElseThrow(
                () -> new CustomException(GROUP_NOT_JOINED)
        );

        HomeworkSubject homeworkSubject = homeworkSubjectRepository.findByUserIdAndGroupIdAndHomeworkId(user.getId(), groupId, homework.getId());

        if (homeworkSubject.isSubmitCheck() == true) {
            throw new CustomException(HOMEWORK_SUBMIT_CHECK_TRUE);
        }

        if (homeworkSubject == null) {
            throw new CustomException(HOMEWORK_FILE_NOT_FOUND);
        }

        if (homeworkSubject.getHomeworkSubjectFileList().size() > 0) {
            try {
                for (HomeworkSubjectFile file : homeworkSubject.getHomeworkSubjectFileList()) {
                    String source = URLDecoder.decode(file.getFileUrl().replace("https://s3://project-navis/image/", ""), "UTF-8");
                    s3Uploader.delete(source);
                }
            } catch (UnsupportedEncodingException e) {
                throw new CustomException(HOMEWORK_FILE_IS_NULL);
            }
        }
        homeworkSubjectRepository.deleteById(homeworkSubject.getId());
        return Message.toResponseEntity(HOMEWORK_SUBMIT_CANCEL);
    }

    @Transactional
    public ResponseEntity<Message> homeworkFeedback(Long groupId, Long boardId, Long subjectId, FeedbackRequestDto requestDto, User user) {
        Group group = groupRepository.findById(groupId).orElseThrow(
                () -> new CustomException(GROUP_NOT_FOUND)
        );

        Homework homework = homeworkRepository.findById(boardId).orElseThrow(
                () -> new CustomException(BOARD_NOT_FOUND)
        );

        user = userRepository.findByUsername(user.getUsername()).orElseThrow(
                () -> new CustomException(MEMBER_NOT_FOUND)
        );

        HomeworkSubject subject = homeworkSubjectRepository.findById(subjectId).orElseThrow(
                () -> new CustomException(HOMEWORK_FILE_NOT_FOUND)
        );

        GroupMember groupMember = groupMemberRepository.findByUserAndGroup(user, group).orElseThrow(
                () -> new CustomException(GROUP_NOT_JOINED)
        );

        if (!groupMember.getGroupRole().equals(GroupMemberRoleEnum.ADMIN) && !groupMember.getGroupRole().equals(GroupMemberRoleEnum.SUPPORT)) {
            throw new CustomException(ADMIN_ONLY);
        }

        Feedback feedback = new Feedback(requestDto.getFeedback(), subject);
        feedbackRepository.save(feedback);

        if (requestDto.isSubmitCheck() == true) {
            subject.submitCheck(true);
            return Message.toResponseEntity(HOMEWORK_SUBMIT_CHECK_SUCCESS);
        } else {
            subject.submitCheck(false);
            return Message.toResponseEntity(HOMEWORK_SUBMIT_CHECK_RETURN_SUCCESS);
        }
    }

//    @Transactional
//    public ResponseEntity<Message> downloadFile(Long groupId, Long boardId, String fileName) throws IOException {
//        return Message.toResponseEntity(FILE_DOWNLOAD_SUCCESS,s3Service.getObject(fileName));
//    }

    public LocalDateTime unixTimeToLocalDateTime(Long unixTime) {
        return LocalDateTime.ofEpochSecond(unixTime, 6, ZoneId.of("Asia/Seoul").getRules().getOffset(Instant.now()));
    }

    public boolean expirationCheck(LocalDateTime dbTime) {
        return LocalDateTime.now().isAfter(dbTime);
    }
}
