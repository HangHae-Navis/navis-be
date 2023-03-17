package com.hanghae.navis.homework.service;

import com.hanghae.navis.board.dto.*;
import com.hanghae.navis.board.repository.BoardRepository;
import com.hanghae.navis.common.config.S3Uploader;
import com.hanghae.navis.common.dto.CustomException;
import com.hanghae.navis.common.dto.Message;
import com.hanghae.navis.common.entity.File;
import com.hanghae.navis.common.entity.Hashtag;
import com.hanghae.navis.common.entity.SuccessMessage;
import com.hanghae.navis.common.repository.FileRepository;
import com.hanghae.navis.common.repository.HashtagRepository;
import com.hanghae.navis.group.entity.Group;
import com.hanghae.navis.group.repository.GroupRepository;
import com.hanghae.navis.homework.dto.HomeworkListResponseDto;
import com.hanghae.navis.homework.dto.HomeworkRequestDto;
import com.hanghae.navis.homework.dto.HomeworkResponseDto;
import com.hanghae.navis.homework.dto.HomeworkUpdateRequestDto;
import com.hanghae.navis.homework.entity.Homework;
import com.hanghae.navis.homework.repository.HomeworkRepository;
import com.hanghae.navis.user.entity.User;
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
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

import static com.hanghae.navis.common.entity.ExceptionMessage.*;
import static com.hanghae.navis.common.entity.SuccessMessage.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class HomeworkService {
    private final HomeworkRepository homeworkRepository;
    private final FileRepository fileRepository;
    private final UserRepository userRepository;
    private final GroupRepository groupRepository;
    private final HashtagRepository hashtagRepository;
    private final S3Uploader s3Uploader;

    @Transactional(readOnly = true)
    public ResponseEntity<Message> homeworkList(Long groupId, User user) {
        Group group = groupRepository.findById(groupId).orElseThrow(
                () -> new CustomException(GROUP_NOT_FOUND)
        );

        user = userRepository.findByUsername(user.getUsername()).orElseThrow(
                () -> new CustomException(MEMBER_NOT_FOUND)
        );

        List<HomeworkListResponseDto> responseList = new ArrayList<>();

        List<Homework> homeworkList = homeworkRepository.findAllByGroupIdOrderByCreatedAtDesc(groupId);

        for(Homework homework : homeworkList) {
            responseList.add(new HomeworkListResponseDto(homework));
        }
        return Message.toResponseEntity(BOARD_LIST_GET_SUCCESS, responseList);
    }

    @Transactional(readOnly = true)
    public ResponseEntity<Message> getHomework(Long groupId, Long boardId, User user) {
        Group group = groupRepository.findById(groupId).orElseThrow(
                () -> new CustomException(GROUP_NOT_FOUND)
        );

        Homework homework = homeworkRepository.findById(boardId).orElseThrow(
                () -> new CustomException(BOARD_NOT_FOUND)
        );

        user = userRepository.findByUsername(user.getUsername()).orElseThrow(
                () -> new CustomException(MEMBER_NOT_FOUND)
        );

        List<FileResponseDto> responseList = new ArrayList<>();

        for (File file : homework.getFileList()) {
            responseList.add(new FileResponseDto(file.getFileTitle(), file.getFileTitle()));
        }
        HomeworkResponseDto homeworkResponseDto = new HomeworkResponseDto(homework, responseList, null, expirationCheck(homework.getExpirationDate()), homework.getExpirationDate());

        return Message.toResponseEntity(BOARD_DETAIL_GET_SUCCESS, homeworkResponseDto);
    }

    @Transactional
    public ResponseEntity<Message> creatHomework(Long groupId, HomeworkRequestDto requestDto, List<MultipartFile> multipartFiles, User user) {
        Group group = groupRepository.findById(groupId).orElseThrow(
                () -> new CustomException(GROUP_NOT_FOUND)
        );

        user = userRepository.findByUsername(user.getUsername()).orElseThrow(
                () -> new CustomException(MEMBER_NOT_FOUND)
        );

        Homework homework = new Homework(requestDto, user, group, unixTimeToLocalDateTime(requestDto.getExpirationDate()), false);

        homeworkRepository.save(homework);

        List<HashtagResponseDto> hashtagResponseDto = new ArrayList<>();

        for(HashtagRequestDto hashtagRequestDto : requestDto.getHashtagList()) {
            String tag = hashtagRequestDto.getHashtag();
            Hashtag hashtag = new Hashtag(tag, homework);
            hashtagRepository.save(hashtag);
            hashtagResponseDto.add(new HashtagResponseDto(tag));
        }

        List<FileResponseDto> fileResponseDto = new ArrayList<>();

        try {
            for (MultipartFile file : multipartFiles) {
                String fileTitle = file.getOriginalFilename();
                String fileUrl = s3Uploader.upload(file);
                File homeworkFile = new File(fileTitle, fileUrl, homework);
                fileRepository.save(homeworkFile);
                fileResponseDto.add(new FileResponseDto(homeworkFile.getFileTitle(), homeworkFile.getFileUrl()));
            }
            HomeworkResponseDto responseDto = new HomeworkResponseDto(homework, fileResponseDto, hashtagResponseDto,false, unixTimeToLocalDateTime(requestDto.getExpirationDate()));

            return Message.toResponseEntity(SuccessMessage.BOARD_POST_SUCCESS, responseDto);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public ResponseEntity<Message> updateHomework(Long groupId, Long boardId, HomeworkUpdateRequestDto requestDto, List<MultipartFile> multipartFiles, User user) {
        Group group = groupRepository.findById(groupId).orElseThrow(
                () -> new CustomException(GROUP_NOT_FOUND)
        );

        Homework homework = homeworkRepository.findById(boardId).orElseThrow(
                () -> new CustomException(BOARD_NOT_FOUND)
        );

        user = userRepository.findByUsername(user.getUsername()).orElseThrow(
                () -> new CustomException(MEMBER_NOT_FOUND)
        );

        if(!user.getUsername().equals(homework.getUser().getUsername())) {
            throw new CustomException(UNAUTHORIZED_UPDATE_OR_DELETE);
        }

        HomeworkResponseDto responseDto = new HomeworkResponseDto(homework, null, null, expirationCheck(homework.getExpirationDate()), homework.getExpirationDate());

        homework.update(requestDto, unixTimeToLocalDateTime(requestDto.getExpirationDate()));

        List<String> remainUrl = requestDto.getUpdateUrlList();

        List<File> files = fileRepository.findFileUrlByBasicBoardId(boardId);

        try {
            for(File boardFile : files) {
                if(!remainUrl.contains(boardFile.getFileUrl())) {
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
            if(multipartFiles != null) {
                List<FileResponseDto> fileResponseDto = new ArrayList<>();

                if(!multipartFiles.isEmpty() && !multipartFiles.get(0).isEmpty()) {
                    for (MultipartFile file : multipartFiles) {
                        String fileTitle = file.getOriginalFilename();
                        String fileUrl = s3Uploader.upload(file);
                        File homeworkFile = new File(fileTitle, fileUrl, homework);
                        fileRepository.save(homeworkFile);
                        fileResponseDto.add(new FileResponseDto(homeworkFile.getFileTitle(), homeworkFile.getFileUrl()));
                    }
                    responseDto = new HomeworkResponseDto(homework, fileResponseDto, null, expirationCheck(homework.getExpirationDate()), homework.getExpirationDate());
                }
            }
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

        Homework homework = homeworkRepository.findById(boardId).orElseThrow(
                () -> new CustomException(BOARD_NOT_FOUND)
        );

        user = userRepository.findByUsername(user.getUsername()).orElseThrow(
                () -> new CustomException(MEMBER_NOT_FOUND)
        );

        if(!user.getId().equals(homework.getUser().getId())) {
            throw new CustomException(UNAUTHORIZED_UPDATE_OR_DELETE);
        }

        if(homework.getFileList().size() > 0) {
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

    public LocalDateTime unixTimeToLocalDateTime(Long unixTime) {
        return LocalDateTime.ofEpochSecond(unixTime, 6, ZoneOffset.UTC);
    }

    public boolean expirationCheck(LocalDateTime dbTime) {
        return LocalDateTime.now().isAfter(dbTime);
    }
}
