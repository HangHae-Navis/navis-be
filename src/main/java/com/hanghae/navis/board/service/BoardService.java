package com.hanghae.navis.board.service;

import com.hanghae.navis.board.dto.*;
import com.hanghae.navis.board.entity.Board;
import com.hanghae.navis.common.dto.*;
import com.hanghae.navis.common.entity.File;
import com.hanghae.navis.common.entity.Hashtag;
import com.hanghae.navis.common.repository.FileRepository;
import com.hanghae.navis.board.repository.BoardRepository;
import com.hanghae.navis.common.config.S3Uploader;
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
import com.hanghae.navis.user.entity.User;
import com.hanghae.navis.user.entity.UserRoleEnum;
import com.hanghae.navis.user.repository.UserRepository;
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
import java.util.ArrayList;
import java.util.List;

import static com.hanghae.navis.common.entity.ExceptionMessage.*;
import static com.hanghae.navis.common.entity.SuccessMessage.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class BoardService {
    private final GroupMemberRepository groupMemberRepository;
    private final BoardRepository boardRepository;
    private final FileRepository fileRepository;
    private final UserRepository userRepository;
    private final GroupRepository groupRepository;
    private final HashtagRepository hashtagRepository;
    private final S3Uploader s3Uploader;
    private final RecentlyViewedRepository recentlyViewedRepository;
    private final QueryRepository queryRepository;


    @Transactional(readOnly = true)
    public ResponseEntity<Message> boardList(Long groupId, int page, int size, User user) {
        Group group = groupRepository.findById(groupId).orElseThrow(
                () -> new CustomException(GROUP_NOT_FOUND)
        );

        user = userRepository.findByUsername(user.getUsername()).orElseThrow(
                () -> new CustomException(MEMBER_NOT_FOUND)
        );

        Pageable pageable = PageRequest.of(page, size);

        Page<Board> boardList = boardRepository.findAllByGroupIdOrderByCreatedAtDesc(groupId, pageable);

        Page<BoardListResponseDto> boardListResponseDto = BoardListResponseDto.toDtoPage(boardList);

        return Message.toResponseEntity(BOARD_LIST_GET_SUCCESS, boardListResponseDto);
    }

    @Transactional
    public ResponseEntity<Message> getBoard(Long groupId, Long boardId, User user) {
        UserGroup userGroup = authCheck(groupId, user);

        Board board = boardRepository.findById(boardId).orElseThrow(
                () -> new CustomException(BOARD_NOT_FOUND)
        );

        GroupMember groupMember = groupMemberRepository.findByUserAndGroup(userGroup.getUser(), userGroup.getGroup()).orElseThrow(
                () -> new CustomException(MEMBER_NOT_FOUND)
        );

        GroupMemberRoleEnum role = groupMember.getGroupRole();

        GroupMemberRoleEnum authorRole = groupMemberRepository.findByUserAndGroup(board.getUser(), board.getGroup()).orElseThrow(
                () -> new CustomException(GROUP_NOT_JOINED)
        ).getGroupRole();

        boolean author = user.equals(board.getUser());

        //최근 본 글 목록 추가용
        RecentlyViewed recentlyViewed = new RecentlyViewed(groupMember, board);
        recentlyViewedRepository.save(recentlyViewed);

        List<RecentlyViewedDto> rvList = queryRepository.findRecentlyViewedsByGroupMemeber(groupMember.getId());

        List<FileResponseDto> fileResponseDto = new ArrayList<>();
        List<String> hashtagResponseDto = new ArrayList<>();
        board.getFileList().forEach(value -> fileResponseDto.add(FileResponseDto.of(value)));
        board.getHashtagList().forEach(value -> hashtagResponseDto.add(value.getHashtagName()));
        BoardResponseDto boardResponseDto = BoardResponseDto.of(board, fileResponseDto, hashtagResponseDto, role, rvList, authorRole, author);

        return Message.toResponseEntity(BOARD_DETAIL_GET_SUCCESS, boardResponseDto);
    }

    @Transactional
    public ResponseEntity<Message> createBoard(Long groupId, BoardRequestDto requestDto, User user) {
        try {
            UserGroup userGroup = authCheck(groupId, user);

            GroupMember groupMember = groupMemberRepository.findByUserAndGroup(userGroup.getUser(), userGroup.getGroup()).orElseThrow(
                    () -> new CustomException(MEMBER_NOT_FOUND)
            );

            GroupMemberRoleEnum role = groupMember.getGroupRole();

            if (groupId == 20 && role.equals(GroupMemberRoleEnum.USER)) {
                return Message.toExceptionResponseEntity(UNAUTHORIZED_ADMIN);
            }

            Board board = new Board(requestDto, userGroup.getUser(), userGroup.getGroup());
            boardRepository.save(board);

            List<String> hashtagResponseDto = new ArrayList<>();

            for (String tag : requestDto.getHashtagList().split(" ")) {
                if (tag.length() > 10) {
                    throw new CustomException(HASHTAG_LENGTH_ERROR);
                }
                Hashtag hashtag = new Hashtag(tag, board);
                hashtagRepository.save(hashtag);
                hashtagResponseDto.add(tag);
            }

            List<FileResponseDto> fileResponseDto = new ArrayList<>();

            if (requestDto.getMultipartFiles() != null) {
                for (MultipartFile file : requestDto.getMultipartFiles()) {
                    String fileTitle = file.getOriginalFilename();
                    String fileUrl = s3Uploader.upload(file);
                    File boardFile = new File(fileTitle, fileUrl, board);
                    fileRepository.save(boardFile);
                    fileResponseDto.add(FileResponseDto.of(boardFile));
                }
            } else {
                fileResponseDto = null;
            }

            RecentlyViewed rv = new RecentlyViewed(groupMember, board);
            recentlyViewedRepository.save(rv);

            List<RecentlyViewedDto> rvList = queryRepository.findRecentlyViewedsByGroupMemeber(groupMember.getId());

            BoardResponseDto boardResponseDto = BoardResponseDto.of(board, fileResponseDto, hashtagResponseDto, role, rvList, groupMember.getGroupRole(), true);

            return Message.toResponseEntity(BOARD_POST_SUCCESS, boardResponseDto);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public ResponseEntity<Message> updateBoard(Long groupId, Long boardId, BoardRequestDto requestDto, User user) {
        UserGroup userGroup = authCheck(groupId, user);

        Board board = boardRepository.findById(boardId).orElseThrow(
                () -> new CustomException(BOARD_NOT_FOUND)
        );

        GroupMember groupMember = groupMemberRepository.findByUserAndGroup(userGroup.getUser(), userGroup.getGroup()).orElseThrow(
                () -> new CustomException(MEMBER_NOT_FOUND)
        );

        GroupMemberRoleEnum role = groupMember.getGroupRole();

        GroupMemberRoleEnum authorRole = groupMemberRepository.findByUserAndGroup(board.getUser(), board.getGroup()).orElseThrow(
                () -> new CustomException(GROUP_NOT_JOINED)
        ).getGroupRole();

        boolean author = user.equals(board.getUser());

        if (!user.getId().equals(board.getUser().getId())) {
            throw new CustomException(UNAUTHORIZED_UPDATE_OR_DELETE);
        }

        List<RecentlyViewedDto> rv = queryRepository.findRecentlyViewedsByGroupMemeber(groupMember.getId());

        BoardResponseDto boardResponseDto = BoardResponseDto.of(board, null, null, role, rv, authorRole, author);

        board.update(requestDto);

        List<Hashtag> remainTag = hashtagRepository.findAllByBasicBoardId(boardId);

        for (Hashtag hashtag : remainTag) {
            board.getFileList().remove(hashtag);
            hashtagRepository.delete(hashtag);
        }

        List<String> hashtagResponseDto = new ArrayList<>();

        for (String tag : requestDto.getHashtagList().split(" ")) {
            if (tag.length() > 10) {
                throw new CustomException(HASHTAG_LENGTH_ERROR);
            }
            Hashtag hashtag = new Hashtag(tag, board);
            hashtagRepository.save(hashtag);
            hashtagResponseDto.add(tag);
        }

        List<MultipartFile> multipartFiles = requestDto.getMultipartFiles();
        List<File> files = fileRepository.findFileUrlByBasicBoardId(boardId);

        try {
            for (File boardFile : files) {
                if (!multipartFiles.contains(boardFile.getFileUrl())) {
                    board.getFileList().remove(boardFile);
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
                    File boardFile = new File(fileTitle, fileUrl, board);
                    fileRepository.save(boardFile);
                    fileResponseDto.add(new FileResponseDto(boardFile.getFileTitle(), boardFile.getFileUrl()));
                }
            } else {
                fileResponseDto = null;
            }
            boardResponseDto = BoardResponseDto.of(board, fileResponseDto, hashtagResponseDto, role, rv, authorRole, author);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return Message.toResponseEntity(BOARD_PUT_SUCCESS, boardResponseDto);
    }

    @Transactional
    public ResponseEntity<Message> deleteBoard(Long groupId, Long boardId, User user) {
        UserGroup userGroup = authCheck(groupId, user);

        Board board = boardRepository.findById(boardId).orElseThrow(
                () -> new CustomException(BOARD_NOT_FOUND)
        );

        GroupMember groupMember = groupMemberRepository.findByUserAndGroup(userGroup.getUser(), userGroup.getGroup()).orElseThrow(
                () -> new CustomException(MEMBER_NOT_FOUND)
        );

        GroupMemberRoleEnum role = groupMember.getGroupRole();

        if (board.getUser().getId().equals(user.getId()) || (role.equals(GroupMemberRoleEnum.ADMIN) || role.equals(GroupMemberRoleEnum.SUPPORT))) {
            if (board.getFileList().size() > 0) {
                try {
                    for (File file : board.getFileList()) {
                        String source = URLDecoder.decode(file.getFileUrl().replace("https://s3://project-navis/image/", ""), "UTF-8");
                        s3Uploader.delete(source);
                    }
                } catch (UnsupportedEncodingException e) {
                    throw new RuntimeException(e);
                }
            }
            boardRepository.deleteById(boardId);
            return Message.toResponseEntity(BOARD_DELETE_SUCCESS);
        }
        throw new CustomException(UNAUTHORIZED_UPDATE_OR_DELETE);
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
}
