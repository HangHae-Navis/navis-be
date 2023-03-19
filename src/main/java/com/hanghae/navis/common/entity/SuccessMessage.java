package com.hanghae.navis.common.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum SuccessMessage {

    SIGN_UP_SUCCESS(HttpStatus.CREATED, "회원가입이 완료 되었습니다."),
    LOGIN_SUCCESS(HttpStatus.OK,"로그인이 완료 되었습니다."),
    USER_INFO_SUCCESS(HttpStatus.OK, "유저정보 불러오기 성공"),
    BOARD_GET_SUCCESS(HttpStatus.OK,"게시물 랜덤 보기 완료"),
    GROUP_CREATE_SUCCESS(HttpStatus.CREATED, "그룹 생성 완료"),
    GROUP_APPLY_SUCCESS(HttpStatus.CREATED, "그룹 가입 완료"),
    GROUPS_GET_SUCCESS(HttpStatus.OK, "그룹 리스트 조회 성공"),
    GROUP_MAIN_PAGE_GET_SUCCESS(HttpStatus.OK, "그룹 메인페이지 조회 성공"),
    GROUP_DETAILS_GET_SUCCESS(HttpStatus.OK, "그룹 세부사항 조회 성공"),
    MEMBER_DELETE_SUCCESS(HttpStatus.OK,"해당 회원의 탈퇴 처리 성공"),
    GROUP_QUIT_SUCCESS(HttpStatus.OK,"그룹 탈퇴 완료"),
    GROUP_DELETE_SUCCESS(HttpStatus.OK, "그룹 삭제 완료"),
    BOARD_POST_SUCCESS(HttpStatus.CREATED, "게시물 작성 완료"),
    BOARD_PUT_SUCCESS(HttpStatus.CREATED,"게시물 수정 완료"),
    BOARD_DELETE_SUCCESS(HttpStatus.CREATED,"게시물 삭제 완료"),
    BOARD_MY_LIST_GET_SUCCESS(HttpStatus.OK,"나의 게시물 보기 완료"),
    BOARD_DETAIL_GET_SUCCESS(HttpStatus.OK,"상세 게시물 보기 완료"),
    COMMENT_POST_SUCCESS(HttpStatus.CREATED,"댓글 작성 완료"),
    COMMENT_UPDATE_SUCCESS(HttpStatus.CREATED,"댓글 작성 완료"),
    COMMENT_LIST_GET_SUCCESS(HttpStatus.CREATED,"댓글 작성 완료"),
    COMMENT_DELETE_SUCCESS(HttpStatus.CREATED, "댓글 삭제 완료"),
    LIKE_POST_SUCCESS(HttpStatus.CREATED, "좋아요 등록 완료"),
    LIKE_DELETE_SUCCESS(HttpStatus.CREATED, "좋아요 취소 완료"),
    EMAIL_SEND_SUCCESS(HttpStatus.CREATED, "메일 전송 완료"),
    EMAIL_CONFIRM_SUCCESS(HttpStatus.OK, "메일 인증 완료"),
    BOARD_LIST_GET_SUCCESS(HttpStatus.OK, "게시글 리스트 조회 성공"),
    VOTE_PICK_SUCCESS(HttpStatus.OK, "투표 등록 성공"),
    VOTE_CANCEL_SUCCESS(HttpStatus.OK, "투표 취소 성공"),
    VOTE_FORCE_EXPIRED_SUCCESS(HttpStatus.OK,"투표 강제 만료 성공"),
    HASHTAG_DELETE_SUCCESS(HttpStatus.OK, "해시태그 삭제 성공"),
    HOMEWORK_SUBMIT_SUCCESS(HttpStatus.OK, "과제 제출 성공"),
    HOMEWORK_SUBMIT_CANCEL(HttpStatus.OK, "과제 제출 취소");
    private final HttpStatus httpStatus;
    private final String detail;
}
