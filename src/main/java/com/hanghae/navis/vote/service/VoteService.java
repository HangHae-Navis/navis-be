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
import com.hanghae.navis.vote.dto.OptionRequestDto;
import com.hanghae.navis.vote.dto.OptionResponseDto;
import com.hanghae.navis.vote.dto.VoteRequestDto;
import com.hanghae.navis.vote.dto.VoteResponseDto;
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

import java.util.ArrayList;
import java.util.List;

import static com.hanghae.navis.common.entity.ExceptionMessage.GROUP_NOT_FOUND;
import static com.hanghae.navis.common.entity.ExceptionMessage.MEMBER_NOT_FOUND;
import static com.hanghae.navis.common.entity.SuccessMessage.BOARD_POST_SUCCESS;

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
    @Transactional
    public ResponseEntity<Message> createVote(Long groupId, VoteRequestDto requestDto, List<MultipartFile> multipartFiles, User user) {
        try {
            user = userRepository.findByUsername(user.getUsername()).orElseThrow(
                    () -> new CustomException(MEMBER_NOT_FOUND)
            );

            Group group = groupRepository.findById(groupId).orElseThrow(
                    () -> new CustomException(GROUP_NOT_FOUND)
            );
            log.warn(requestDto.getContent());
            Vote vote = new Vote(requestDto, user, group);
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

            VoteResponseDto voteResponseDto = new VoteResponseDto(vote, fileResponseDto, optionResponseDto);
            return Message.toResponseEntity(BOARD_POST_SUCCESS, voteResponseDto);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
