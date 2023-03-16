package com.hanghae.navis.vote.service;

import com.hanghae.navis.board.dto.FileResponseDto;
import com.hanghae.navis.common.config.S3Uploader;
import com.hanghae.navis.common.dto.CustomException;
import com.hanghae.navis.common.dto.Message;
import com.hanghae.navis.common.entity.File;
import com.hanghae.navis.common.repository.FileRepository;
import com.hanghae.navis.group.entity.Group;
import com.hanghae.navis.group.repository.GroupRepository;
import com.hanghae.navis.user.entity.User;
import com.hanghae.navis.user.repository.UserRepository;
import com.hanghae.navis.vote.dto.*;
import com.hanghae.navis.vote.entity.Vote;
import com.hanghae.navis.vote.entity.VoteOption;
import com.hanghae.navis.vote.repository.VoteOptionRepository;
import com.hanghae.navis.vote.repository.VoteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

import static com.hanghae.navis.common.entity.ExceptionMessage.*;
import static com.hanghae.navis.common.entity.SuccessMessage.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class VoteService {
    private final UserRepository userRepository;
    private final GroupRepository groupRepository;
    private final VoteRepository voteRepository;
    private final VoteOptionRepository voteOptionRepository;
    private final FileRepository fileRepository;

    private final S3Uploader s3Uploader;

    @Transactional(readOnly = true)
    public ResponseEntity<Message> getVoteList(Long groupId, User user) {
        Group group = groupRepository.findById(groupId).orElseThrow(
                () -> new CustomException(GROUP_NOT_FOUND)
        );

        user = userRepository.findByUsername(user.getUsername()).orElseThrow(
                () -> new CustomException(MEMBER_NOT_FOUND)
        );

        List<VoteListResponseDto> responseList = new ArrayList<>();

        List<Vote> voteList = voteRepository.findAllByGroupIdOrderByCreatedAtDesc(groupId);

        for (Vote vote : voteList) {
            responseList.add(new VoteListResponseDto(vote));
        }
        return Message.toResponseEntity(BOARD_LIST_GET_SUCCESS, responseList);
    }

    @Transactional(readOnly = true)
    public ResponseEntity<Message> getVote(Long groupId, Long voteId, User user) {
        Group group = groupRepository.findById(groupId).orElseThrow(
                () -> new CustomException(GROUP_NOT_FOUND)
        );

        Vote vote = voteRepository.findById(voteId).orElseThrow(
                () -> new CustomException(BOARD_NOT_FOUND)
        );

        List<OptionResponseDto> optionResponseDto = new ArrayList<>();
        for(VoteOption voteOption : vote.getVoteOptionList()){
            optionResponseDto.add(new OptionResponseDto(voteOption.getOption(), (long) voteOption.getVoteRecordList().size()));
        }
        user = userRepository.findByUsername(user.getUsername()).orElseThrow(
                () -> new CustomException(MEMBER_NOT_FOUND)
        );

        List<FileResponseDto> fileResponseDto = new ArrayList<>();
        for (File file: vote.getFileList()) {
            fileResponseDto.add(new FileResponseDto(file.getFileTitle(), file.getFileUrl()));
        }
        VoteResponseDto voteResponseDto = new VoteResponseDto(vote, fileResponseDto, optionResponseDto, expirationCheck(
                vote.getExpirationDate()),vote.getExpirationDate());
        return Message.toResponseEntity(BOARD_DETAIL_GET_SUCCESS, voteResponseDto);
    }



    @Transactional
    public ResponseEntity<Message> createVote(Long groupId, VoteRequestDto requestDto, List<MultipartFile> multipartFiles, User user) {
        try {
            user = userRepository.findByUsername(user.getUsername()).orElseThrow(
                    () -> new CustomException(MEMBER_NOT_FOUND)
            );

            Group group = groupRepository.findById(groupId).orElseThrow(
                    () -> new CustomException(GROUP_NOT_FOUND)
            );

            Vote vote = new Vote(requestDto, user, group, unixTimeToLocalDateTime(requestDto.getExpirationDate()),false);
            voteRepository.saveAndFlush(vote);
            List<FileResponseDto> fileResponseDto = new ArrayList<>();
            for (MultipartFile file : multipartFiles) {
                String fileTitle = file.getOriginalFilename();
                String fileUrl = s3Uploader.upload(file);
                File voteFile = new File(fileTitle, fileUrl, vote);
                fileRepository.save(voteFile);
                fileResponseDto.add(new FileResponseDto(voteFile.getFileTitle(), voteFile.getFileUrl()));
            }

            List<OptionResponseDto> optionResponseDto = new ArrayList<>();
            for(OptionRequestDto optionRequestDtoList : requestDto.getOptionRequestDto())
            {
                String option = optionRequestDtoList.getOption();
                VoteOption voteOption = new VoteOption(vote, option);
                voteOptionRepository.save(voteOption);
                optionResponseDto.add(new OptionResponseDto(option, 0L));
            }

            VoteResponseDto voteResponseDto = new VoteResponseDto(vote, fileResponseDto, optionResponseDto, false, unixTimeToLocalDateTime(requestDto.getExpirationDate()));
            return Message.toResponseEntity(BOARD_POST_SUCCESS, voteResponseDto);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public LocalDateTime unixTimeToLocalDateTime(Long unixTime){
        return LocalDateTime.ofEpochSecond(unixTime, 6, ZoneOffset.UTC);
    }

    public boolean expirationCheck(LocalDateTime dbTime){
        return LocalDateTime.now().isAfter(dbTime);
    }
}
