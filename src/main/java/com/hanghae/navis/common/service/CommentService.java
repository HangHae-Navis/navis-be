package com.hanghae.navis.common.service;

import com.hanghae.navis.common.entity.BasicBoard;
import com.hanghae.navis.common.entity.Comment;
import com.hanghae.navis.common.dto.CommentRequestDto;
import com.hanghae.navis.common.dto.CommentResponseDto;
import com.hanghae.navis.common.repository.CommentRepository;
import com.hanghae.navis.common.dto.CustomException;
import com.hanghae.navis.common.dto.Message;
import com.hanghae.navis.common.repository.BasicBoardRepository;
import com.hanghae.navis.group.entity.Group;
import com.hanghae.navis.group.entity.GroupMember;
import com.hanghae.navis.group.entity.GroupMemberRoleEnum;
import com.hanghae.navis.group.repository.GroupMemberRepository;
import com.hanghae.navis.group.repository.GroupRepository;
import com.hanghae.navis.user.entity.User;
import com.hanghae.navis.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.hanghae.navis.common.entity.ExceptionMessage.*;
import static com.hanghae.navis.common.entity.SuccessMessage.*;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final BasicBoardRepository basicBoardRepository;

    @Transactional(readOnly = true)
    public ResponseEntity<Message> commentList(Long groupId, Long boardId, int page, int size, User user) {
        Group group = groupRepository.findById(groupId).orElseThrow(
                () -> new CustomException(GROUP_NOT_FOUND)
        );

        BasicBoard basicBoard = basicBoardRepository.findById(boardId).orElseThrow(
                () -> new CustomException(BOARD_NOT_FOUND)
        );

        user = userRepository.findByUsername(user.getUsername()).orElseThrow(
                () -> new CustomException(MEMBER_NOT_FOUND)
        );

        GroupMember groupMember = groupMemberRepository.findByUserAndGroup(user, group).orElseThrow(
                () -> new CustomException(GROUP_NOT_JOINED)
        );

        Pageable pageable = PageRequest.of(page, size);
        Page<Comment> commentPage = commentRepository.findAllByBasicBoardIdOrderByCreatedAt(boardId, pageable);

        Page<CommentResponseDto> commentResponseDto = CommentResponseDto.toDtoPage(commentPage, user);

        return Message.toResponseEntity(COMMENT_LIST_GET_SUCCESS, commentResponseDto);
    }

    @Transactional
    public ResponseEntity<Message> createComment(Long groupId, Long boardId, CommentRequestDto requestDto, User user) {
        Group group = groupRepository.findById(groupId).orElseThrow(
                () -> new CustomException(GROUP_NOT_FOUND)
        );

        BasicBoard basicBoard = basicBoardRepository.findById(boardId).orElseThrow(
                () -> new CustomException(BOARD_NOT_FOUND)
        );

        user = userRepository.findByUsername(user.getUsername()).orElseThrow(
                () -> new CustomException(MEMBER_NOT_FOUND)
        );

        GroupMember groupMember = groupMemberRepository.findByUserAndGroup(user, group).orElseThrow(
                () -> new CustomException(GROUP_NOT_JOINED)
        );

        if(requestDto.getContent() != null) {
            Comment comment = new Comment(requestDto, user, basicBoard);

            commentRepository.save(comment);

            CommentResponseDto commentResponseDto = CommentResponseDto.of(comment, user);
            return Message.toResponseEntity(COMMENT_POST_SUCCESS, commentResponseDto);
        } else {
            throw new CustomException(CONTENT_IS_NULL);
        }
    }

    @Transactional
    public ResponseEntity<Message> updateComment(Long groupId, Long boardId, Long commentId, CommentRequestDto requestDto, User user) {
        Group group = groupRepository.findById(groupId).orElseThrow(
                () -> new CustomException(GROUP_NOT_FOUND)
        );

        BasicBoard basicBoard = basicBoardRepository.findById(boardId).orElseThrow(
                () -> new CustomException(BOARD_NOT_FOUND)
        );

        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new CustomException(COMMENT_NOT_FOUND)
        );

        GroupMember groupMember = groupMemberRepository.findByUserAndGroup(user, group).orElseThrow(
                () -> new CustomException(GROUP_NOT_JOINED)
        );

        if(!groupMember.getGroupRole().equals(GroupMemberRoleEnum.ADMIN) && !groupMember.getGroupRole().equals(GroupMemberRoleEnum.SUPPORT)) {
            throw new CustomException(UNAUTHORIZED_ADMIN);
        }

        if(requestDto.getContent() != null) {
            comment.updateComment(requestDto);
            CommentResponseDto commentResponseDto = CommentResponseDto.of(comment, user);

            return Message.toResponseEntity(COMMENT_UPDATE_SUCCESS, commentResponseDto);
        } else {
            throw new CustomException(CONTENT_IS_NULL);
        }
    }

    @Transactional
    public ResponseEntity<Message> deleteComment(Long groupId, Long boardId, Long commentId, User user) {
        Group group = groupRepository.findById(groupId).orElseThrow(
                () -> new CustomException(GROUP_NOT_FOUND)
        );

        BasicBoard basicBoard = basicBoardRepository.findById(boardId).orElseThrow(
                () -> new CustomException(BOARD_NOT_FOUND)
        );

        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new CustomException(COMMENT_NOT_FOUND)
        );

        user = userRepository.findByUsername(user.getUsername()).orElseThrow(
                () -> new CustomException(MEMBER_NOT_FOUND)
        );

        GroupMember groupMember = groupMemberRepository.findByUserAndGroup(user, group).orElseThrow(
                () -> new CustomException(GROUP_NOT_JOINED)
        );

        if(!groupMember.getGroupRole().equals(GroupMemberRoleEnum.ADMIN) && !groupMember.getGroupRole().equals(GroupMemberRoleEnum.SUPPORT) && !user.getId().equals(comment.getUser().getId())) {
            throw new CustomException(USER_FORBIDDEN);
        }

        commentRepository.deleteById(commentId);

        return Message.toResponseEntity(COMMENT_DELETE_SUCCESS);
    }
}
