package com.hanghae.navis.vote.service;

import com.hanghae.navis.common.dto.FileResponseDto;
import com.hanghae.navis.common.dto.HashtagRequestDto;
import com.hanghae.navis.common.dto.HashtagResponseDto;
import com.hanghae.navis.common.config.S3Uploader;
import com.hanghae.navis.common.dto.CustomException;
import com.hanghae.navis.common.dto.Message;
import com.hanghae.navis.common.dto.UserGroup;
import com.hanghae.navis.common.entity.File;
import com.hanghae.navis.common.entity.Hashtag;
import com.hanghae.navis.common.repository.FileRepository;
import com.hanghae.navis.common.repository.HashtagRepository;
import com.hanghae.navis.group.entity.Group;
import com.hanghae.navis.group.entity.GroupMember;
import com.hanghae.navis.group.repository.GroupMemberRepository;
import com.hanghae.navis.group.repository.GroupRepository;
import com.hanghae.navis.user.entity.User;
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

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
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
    private final GroupMemberRepository groupMemberRepository;

    private final VoteRepository voteRepository;
    private final VoteOptionRepository voteOptionRepository;
    private final VoteRecordRepository voteRecordRepository;
    private final FileRepository fileRepository;
    private final HashtagRepository hashtagRepository;
    private final S3Uploader s3Uploader;

    @Transactional(readOnly = true)
    public ResponseEntity<Message> getVoteList(Long groupId, User user, int page, int size) {
        UserGroup userGroup = authCheck(groupId, user);
        Pageable pageable = PageRequest.of(page, size);

        Page<Vote> voteList = voteRepository.findAllByGroupIdOrderByCreatedAtDesc(groupId, pageable);

        Page<VoteListResponseDto> voteListResponseDto = VoteListResponseDto.toDtoPage(voteList);
        return Message.toResponseEntity(BOARD_LIST_GET_SUCCESS, voteListResponseDto);
    }

    @Transactional(readOnly = true)
    public ResponseEntity<Message> getVote(Long groupId, Long voteId, User user) {
        UserGroup userGroup = authCheck(groupId, user);

        Vote vote = voteRepository.findById(voteId).orElseThrow(
                () -> new CustomException(BOARD_NOT_FOUND)
        );

        List<OptionResponseDto> optionResponseDto = new ArrayList<>();
        for (VoteOption voteOption : vote.getVoteOptionList()) {
            optionResponseDto.add(OptionResponseDto.of(voteOption));
        }
        user = userRepository.findByUsername(user.getUsername()).orElseThrow(
                () -> new CustomException(MEMBER_NOT_FOUND)
        );

        List<FileResponseDto> fileResponseDto = new ArrayList<>();

        vote.getFileList().forEach(value -> fileResponseDto.add(FileResponseDto.of(value)));

        VoteResponseDto voteResponseDto = VoteResponseDto.of(vote, fileResponseDto, null, optionResponseDto, expirationCheck(
                vote.getExpirationDate()), vote.getExpirationDate());
        return Message.toResponseEntity(BOARD_DETAIL_GET_SUCCESS, voteResponseDto);
    }


    @Transactional
    public ResponseEntity<Message> createVote(Long groupId, VoteRequestDto requestDto, List<MultipartFile> multipartFiles, User user) {
        try {
            UserGroup userGroup = authCheck(groupId, user);

            Vote vote = new Vote(requestDto, userGroup.getUser(), userGroup.getGroup(), unixTimeToLocalDateTime(requestDto.getExpirationDate()), false);
            voteRepository.saveAndFlush(vote);

            List<HashtagResponseDto> hashtagResponseDto = new ArrayList<>();

            for(HashtagRequestDto hashtagRequestDto : requestDto.getHashtagList()) {
                String tag = hashtagRequestDto.getHashtag();
                Hashtag hashtag = new Hashtag(tag, vote);
                hashtagRepository.save(hashtag);
                hashtagResponseDto.add(HashtagResponseDto.of(hashtag));
            }

            List<FileResponseDto> fileResponseDto = new ArrayList<>();

            for (MultipartFile file : multipartFiles) {
                String fileTitle = file.getOriginalFilename();
                String fileUrl = s3Uploader.upload(file);
                File voteFile = new File(fileTitle, fileUrl, vote);
                fileRepository.save(voteFile);
                fileResponseDto.add(FileResponseDto.of(voteFile));
            }

            List<OptionResponseDto> optionResponseDto = new ArrayList<>();
            for (OptionRequestDto optionRequestDtoList : requestDto.getOptionRequestDto()) {
                String option = optionRequestDtoList.getOption();
                VoteOption voteOption = new VoteOption(vote, option);
                voteOptionRepository.save(voteOption);
                optionResponseDto.add(OptionResponseDto.of(voteOption));
            }

            VoteResponseDto voteResponseDto = VoteResponseDto.of(vote, fileResponseDto,  hashtagResponseDto,optionResponseDto, false, unixTimeToLocalDateTime(requestDto.getExpirationDate()));

            return Message.toResponseEntity(BOARD_POST_SUCCESS, voteResponseDto);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

//    @Transactional
//    public ResponseEntity<Message> updateVote(Long groupId, Long voteId, VoteRequestDto requestDto, List<MultipartFile> multipartFiles, User user) {
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
//        vote.update(requestDto, unixTimeToLocalDateTime(requestDto.getExpirationDate()));
//
//        List<String> remainUrl = requestDto.getUpdateUrlList();
//
//        List<OptionRequestDto> remainOption = requestDto.getOptionRequestDto();
//        try {
//            for (File file : vote.getFileList()) {
//                if (!remainUrl.contains(file.getFileUrl())) {
//                    vote.getFileList().remove(file);
//                    String source = URLDecoder.decode(file.getFileUrl().replace("https://s3://project-navis/image/", ""), "UTF-8");
//                    s3Uploader.delete(source);
//                    fileRepository.delete(file);
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
        UserGroup userGroup = authCheck(groupId, user);

        Vote vote = voteRepository.findById(voteId).orElseThrow(
                () -> new CustomException(BOARD_NOT_FOUND)
        );

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

    public ResponseEntity<Message> forceExpired(Long groupId, Long voteId, User user) {
        UserGroup userGroup = authCheck(groupId, user);

        Vote vote = voteRepository.findById(voteId).orElseThrow(
                () -> new CustomException(BOARD_NOT_FOUND)
        );


        vote.forceExpiration();

        return Message.toResponseEntity(VOTE_FORCE_EXPIRED_SUCCESS,
                VoteResponseDto.of(vote,
                        parseFileResponseDto(vote.getFileList()),
                        null,
                        parseOptionResponseDto(vote.getVoteOptionList()),
                        vote.isForceExpiration(),
                        vote.getExpirationDate()));
    }

    public ResponseEntity<Message> pickVote(Long groupId, Long voteId, Long voteOptionId, User user) {
        UserGroup userGroup = authCheck(groupId, user);
        Vote vote = voteRepository.findById(voteId).orElseThrow(
                () -> new CustomException(BOARD_NOT_FOUND)
        );

        VoteOption voteOption = voteOptionRepository.findById(voteOptionId).orElseThrow(
                () -> new CustomException(VOTE_OPTION_NOT_FOUND)
        );

        GroupMember groupMember = groupMemberRepository.findByUserAndGroup(userGroup.getUser(), userGroup.getGroup()).orElseThrow(
                () -> new CustomException(GROUP_MEMBER_NOT_FOUND)
        );

        if (!(expirationCheck(vote.getExpirationDate()) || vote.isForceExpiration())) {
            if (voteRecordRepository.findByGroupMemberIdAndVoteOptionId(groupMember.getId(), voteOption.getId()).isPresent()) {
                voteRecordRepository.deleteById(voteRecordRepository.findByGroupMemberIdAndVoteOptionId(groupMember.getId(), voteOption.getId()).get().getId());
                return Message.toResponseEntity(VOTE_CANCEL_SUCCESS,
                        VoteResponseDto.of(vote,
                                parseFileResponseDto(vote.getFileList()),
                                null,
                                parseOptionResponseDto(vote.getVoteOptionList()),
                                vote.isForceExpiration(),
                                vote.getExpirationDate()));

            } else {
                VoteRecord voteRecord = new VoteRecord(voteOption, groupMember);
                voteRecordRepository.save(voteRecord);
                return Message.toResponseEntity(VOTE_PICK_SUCCESS,
                        VoteResponseDto.of(vote,
                                parseFileResponseDto(vote.getFileList()),
                                null,
                                parseOptionResponseDto(vote.getVoteOptionList()),
                                vote.isForceExpiration(),
                                vote.getExpirationDate()));
            }
        } else
            return Message.toExceptionResponseEntity(VOTE_EXPIRED);
    }

    public UserGroup authCheck(Long groupId, User user) {

        Group group = groupRepository.findById(groupId).orElseThrow(
                () -> new CustomException(GROUP_NOT_FOUND)
        );

        user = userRepository.findByUsername(user.getUsername()).orElseThrow(
                () -> new CustomException(MEMBER_NOT_FOUND)
        );

        if (!groupMemberRepository.findByUserAndGroup(user, group).isPresent()) {
            throw new CustomException(GROUP_MEMBER_NOT_FOUND);
        }

        return new UserGroup(user, group);
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
        return LocalDateTime.ofEpochSecond(unixTime, 6, ZoneOffset.UTC);
    }

    public boolean expirationCheck(LocalDateTime dbTime) {
        return LocalDateTime.now().isAfter(dbTime);
    }


}
