package com.hanghae.navis.notice.service;

import com.hanghae.navis.common.config.S3Uploader;
import com.hanghae.navis.common.dto.*;
import com.hanghae.navis.common.entity.File;
import com.hanghae.navis.common.entity.Hashtag;
import com.hanghae.navis.common.repository.FileRepository;
import com.hanghae.navis.common.repository.HashtagRepository;
import com.hanghae.navis.group.entity.Group;
import com.hanghae.navis.group.entity.GroupMember;
import com.hanghae.navis.group.repository.GroupMemberRepository;
import com.hanghae.navis.group.repository.GroupRepository;
import com.hanghae.navis.notice.dto.NoticeListResponseDto;
import com.hanghae.navis.notice.dto.NoticeRequestDto;
import com.hanghae.navis.notice.dto.NoticeResponseDto;
import com.hanghae.navis.notice.dto.NoticeUpdateRequestDto;
import com.hanghae.navis.notice.entity.Notice;
import com.hanghae.navis.notice.repository.NoticeRepository;
import com.hanghae.navis.user.entity.User;
import com.hanghae.navis.user.entity.UserRoleEnum;
import com.hanghae.navis.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import static com.hanghae.navis.common.entity.ExceptionMessage.*;
import static com.hanghae.navis.common.entity.SuccessMessage.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class NoticeService {
    private final NoticeRepository noticeRepository;
    private final FileRepository fileRepository;
    private final UserRepository userRepository;
    private final GroupRepository groupRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final HashtagRepository hashtagRepository;
    private final S3Uploader s3Uploader;


    @Transactional(readOnly = true)
    public ResponseEntity<Message> noticeList(Long groupId, User user) {
        Group group = groupRepository.findById(groupId).orElseThrow(
                () -> new CustomException(GROUP_NOT_FOUND)
        );

        user = userRepository.findByUsername(user.getUsername()).orElseThrow(
                () -> new CustomException(MEMBER_NOT_FOUND)
        );

        List<NoticeListResponseDto> responseList = new ArrayList<>();

        List<Notice> noticeList = noticeRepository.findAllByGroupIdOrderByCreatedAtDesc(groupId);

        List<HashtagResponseDto> tagResponseList = new ArrayList<>();

        for (Notice notice : noticeList) {
            responseList.add(NoticeListResponseDto.of(notice, null));
        }
        return Message.toResponseEntity(BOARD_LIST_GET_SUCCESS, responseList);
    }

    @Transactional(readOnly = true)
    public ResponseEntity<Message> getNotice(Long groupId, Long noticeId, User user) {
        Group group = groupRepository.findById(groupId).orElseThrow(
                () -> new CustomException(GROUP_NOT_FOUND)
        );

        Notice notice = noticeRepository.findById(noticeId).orElseThrow(
                () -> new CustomException(BOARD_NOT_FOUND)
        );

        user = userRepository.findByUsername(user.getUsername()).orElseThrow(
                () -> new CustomException(MEMBER_NOT_FOUND)
        );

        List<FileResponseDto> fileResponseDto = new ArrayList<>();
        List<HashtagResponseDto> hashtagResponseDto = new ArrayList<>();
        notice.getFileList().forEach(value -> fileResponseDto.add(FileResponseDto.of(value)));
        notice.getHashtagList().forEach(value -> hashtagResponseDto.add(HashtagResponseDto.of(value)));
        NoticeResponseDto noticeResponseDto = NoticeResponseDto.of(notice, fileResponseDto, hashtagResponseDto);
        return Message.toResponseEntity(BOARD_DETAIL_GET_SUCCESS, noticeResponseDto);
    }

    @Transactional
    public ResponseEntity<Message> createNotice(Long groupId, NoticeRequestDto requestDto, List<MultipartFile> multipartFiles, User user) {
        try {

            UserGroup userGroup = authCheck(groupId, user);

            Notice notice = new Notice(requestDto, user, userGroup.getGroup());

            noticeRepository.save(notice);

            List<HashtagResponseDto> hashtagResponseDto = new ArrayList<>();

            for (HashtagRequestDto hashtagRequestDto : requestDto.getHashtagList()) {
                String tag = hashtagRequestDto.getHashtag();
                Hashtag hashtag = new Hashtag(tag, notice);
                hashtagRepository.save(hashtag);
                hashtagResponseDto.add(new HashtagResponseDto(tag));
            }

            List<FileResponseDto> fileResponseDto = new ArrayList<>();
            if (multipartFiles != null) {
                for (MultipartFile file : multipartFiles) {
                    String fileTitle = file.getOriginalFilename();
                    String fileUrl = s3Uploader.upload(file);
                    File noticeFile = new File(fileTitle, fileUrl, notice);
                    fileRepository.save(noticeFile);
                    fileResponseDto.add(FileResponseDto.of(noticeFile));
                }
            }
            NoticeResponseDto noticeResponseDto = NoticeResponseDto.of(notice, fileResponseDto, hashtagResponseDto);
            return Message.toResponseEntity(BOARD_POST_SUCCESS, noticeResponseDto);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public ResponseEntity<Message> updateNotice(Long groupId, Long noticeId, NoticeUpdateRequestDto requestDto, List<MultipartFile> multipartFiles, User user) {

        UserGroup userGroup = authCheck(groupId, user);

        Notice notice = noticeRepository.findById(noticeId).orElseThrow(
                () -> new CustomException(BOARD_NOT_FOUND)
        );

        NoticeResponseDto noticeResponseDto = NoticeResponseDto.of(notice, null, null);

        if (!user.getUsername().equals(notice.getUser().getUsername())) {
            throw new CustomException(UNAUTHORIZED_UPDATE_OR_DELETE);
        }

        notice.update(requestDto);

        List<String> remainUrl = requestDto.getUpdateUrlList();

        List<File> files = fileRepository.findFileUrlByBasicBoardId(noticeId);

        try {
            for (File noticeFile : files) {
                if (!remainUrl.contains(noticeFile.getFileUrl())) {
                    notice.getFileList().remove(noticeFile);
                    String source = URLDecoder.decode(noticeFile.getFileUrl().replace("https://s3://project-navis/image/", ""), "UTF-8");
                    s3Uploader.delete(source);
                    fileRepository.delete(noticeFile);
                }
            }

        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

        try {
            if (multipartFiles != null) {
                List<FileResponseDto> fileResponseDto = new ArrayList<>();
                if (!multipartFiles.isEmpty() && !multipartFiles.get(0).isEmpty()) {
                    for (MultipartFile file : multipartFiles) {
                        String fileTitle = file.getOriginalFilename();
                        String fileUrl = s3Uploader.upload(file);
                        File noticeFile = new File(fileTitle, fileUrl, notice);
                        fileRepository.save(noticeFile);
                        fileResponseDto.add(new FileResponseDto(noticeFile.getFileTitle(), noticeFile.getFileUrl()));
                    }
                    noticeResponseDto = NoticeResponseDto.of(notice, fileResponseDto, null);
                }

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return Message.toResponseEntity(BOARD_PUT_SUCCESS, noticeResponseDto);
    }

    @Transactional
    public ResponseEntity<Message> deleteNotice(Long groupId, Long noticeId, User user) {
        UserGroup userGroup = authCheck(groupId, user);

        Notice notice = noticeRepository.findById(noticeId).orElseThrow(
                () -> new CustomException(BOARD_NOT_FOUND)
        );

        if (!user.getId().equals(notice.getUser().getId())) {
            throw new CustomException(UNAUTHORIZED_UPDATE_OR_DELETE);
        }

        if (notice.getFileList().size() > 0) {
            try {
                for (File file : notice.getFileList()) {
                    String source = URLDecoder.decode(file.getFileUrl().replace("https://s3://project-navis/image/", ""), "UTF-8");
                    s3Uploader.delete(source);
                }
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
        }
        noticeRepository.deleteById(noticeId);
        return Message.toResponseEntity(BOARD_DELETE_SUCCESS);
    }

    @Transactional
    public ResponseEntity<Message> deleteHashtag(Long groupId, Long hashtagId, User user) {

        UserGroup userGroup = authCheck(groupId, user);

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

        user = userRepository.findByUsername(user.getUsername()).orElseThrow(
                () -> new CustomException(MEMBER_NOT_FOUND)
        );

        GroupMember groupMember = groupMemberRepository.findByUserAndGroup(user, group).orElseThrow(
                () -> new CustomException(MEMBER_NOT_FOUND)
        );

        if (groupMember.getGroupRole().getAuthority().equals(UserRoleEnum.Authority.USER)) {
            throw new CustomException(UNAUTHORIZED_ADMIN);
        }

        return new UserGroup(user, group);
    }

}