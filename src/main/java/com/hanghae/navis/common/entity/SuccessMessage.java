package com.hanghae.navis.common.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum SuccessMessage {

    SIGN_UP_SUCCESS(HttpStatus.CREATED, "회원가입이 완료 되었습니다."),
    PASSWORD_CHANGE_SUCCESS(HttpStatus.CREATED, "비밀번호가 변경되었습니다."),
    LOGIN_SUCCESS(HttpStatus.OK,"로그인이 완료 되었습니다."),
    USER_INFO_SUCCESS(HttpStatus.OK, "유저정보 불러오기 성공"),
    BOARD_GET_SUCCESS(HttpStatus.OK,"게시물 랜덤 보기 완료"),
    GROUP_CREATE_SUCCESS(HttpStatus.CREATED, "그룹 생성 완료"),
    GROUP_APPLY_SUCCESS(HttpStatus.CREATED, "그룹 가입 완료"),
    GROUPS_GET_SUCCESS(HttpStatus.OK, "그룹 리스트 조회 성공"),
    GROUP_MAIN_PAGE_GET_SUCCESS(HttpStatus.OK, "그룹 메인페이지 조회 성공"),
    GROUP_DETAILS_GET_SUCCESS(HttpStatus.OK, "그룹 세부사항 조회 성공"),
    GROUP_UPDATE_SUCCESS(HttpStatus.OK, "그룹 세부사항 갱신 성공"),
    GROUPCODE_UPDATE_SUCCESS(HttpStatus.OK, "그룹 초대코드 갱신 성공"),
    MEMBER_ROLE_UPDATE_SUCCESS(HttpStatus.OK, "권한 변경 완료"),
    ADMIN_TRANSFER_SUCCESS(HttpStatus.OK, "관리자 변경 완료"),
    MEMBER_DELETE_SUCCESS(HttpStatus.OK,"해당 회원의 탈퇴 처리 성공"),
    MEMBER_UNBAN_SUCCESS(HttpStatus.OK, "차단 해제 성공"),
    GROUP_QUIT_SUCCESS(HttpStatus.OK,"그룹 탈퇴 완료"),
    GROUP_DELETE_SUCCESS(HttpStatus.OK, "그룹 삭제 완료"),
    BOARD_POST_SUCCESS(HttpStatus.CREATED, "게시물 작성 완료"),
    BOARD_PUT_SUCCESS(HttpStatus.CREATED,"게시물 수정 완료"),
    BOARD_DELETE_SUCCESS(HttpStatus.CREATED,"게시물 삭제 완료"),
    BOARD_MY_LIST_GET_SUCCESS(HttpStatus.OK,"나의 게시물 보기 완료"),
    BOARD_DETAIL_GET_SUCCESS(HttpStatus.OK,"상세 게시물 보기 완료"),
    COMMENT_POST_SUCCESS(HttpStatus.CREATED,"댓글 작성 완료"),
    COMMENT_UPDATE_SUCCESS(HttpStatus.CREATED,"댓글 작성 완료"),
    COMMENT_LIST_GET_SUCCESS(HttpStatus.CREATED,"댓글 리스트 조회 완료"),
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
    HOMEWORK_SUBJECT_FILE_UPDATE_SUCCESS(HttpStatus.OK, "과제 파일 수정 성공"),
    HOMEWORK_SUBMIT_CANCEL(HttpStatus.OK, "과제 제출 취소"),
    CHAT_ENTER_SUCCESS(HttpStatus.OK, "채팅 연결 완료"),
    CHAT_READ_SUCCESS(HttpStatus.OK, "채팅 읽음처리 완료"),
    CHAT_ROOM_CREATE_SUCCESS(HttpStatus.CREATED, "채팅방 생성 완료"),
    CHAT_LIST_GET_SUCCESS(HttpStatus.OK, "채팅방 리스트 확인 완료"),
    CHAT_POST_SUCCESS(HttpStatus.OK, "채팅 보내기 완료"),
    HOMEWORK_SUBMIT_LIST_GET_SUCCESS(HttpStatus.OK, "과제 제출자 리스트 조회 성공"),
    FILE_DOWNLOAD_SUCCESS(HttpStatus.OK, "과제 파일 다운로드 성공"),
//    FEEDBACK_POST_SUCCESS(HttpStatus.CREATED, "피드백 작성 성공"),
    HOMEWORK_SUBMIT_CHECK_SUCCESS(HttpStatus.OK, "과제 최종제출 확정 성공"),
    HOMEWORK_SUBMIT_CHECK_RETURN_SUCCESS(HttpStatus.OK, "과제 최종제출 반려 성공"),
    NOTIFICATION_GET_SUCCESS(HttpStatus.OK, "알림 가져오기 성공"),
    NOTIFICATION_DELETE_SUCCESS(HttpStatus.OK, "알림 삭제 성공"),
    SURVEY_POST_SUCCESS(HttpStatus.CREATED, "설문 등록 성공"),
    SURVEY_FORCE_EXPIRED_SUCCESS(HttpStatus.OK,"투표 강제 만료 성공");
    private final HttpStatus httpStatus;
    private final String detail;
}
