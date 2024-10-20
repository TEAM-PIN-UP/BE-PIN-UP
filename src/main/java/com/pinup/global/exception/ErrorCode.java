package com.pinup.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    /* 400 */
    VALIDATION_FAILED(HttpStatus.BAD_REQUEST, "VALIDATION_FAILED", "입력값 유효성 검사에 실패하였습니다."),
    ALREADY_EXIST_EMAIL(HttpStatus.BAD_REQUEST, "ALREADY_EXIST_EMAIL", "이미 가입된 이메일입니다."),
    ALREADY_EXIST_NICKNAME(HttpStatus.BAD_REQUEST, "ALREADY_EXIST_NICKNAME", "중복된 닉네임입니다."),
    INVALID_TOKEN(HttpStatus.BAD_REQUEST, "INVALID_TOKEN", "유효하지 않은 토큰입니다."),
    NOT_EXPIRED_ACCESS_TOKEN(HttpStatus.BAD_REQUEST, "NOT_EXPIRED_ACCESS_TOKEN", "만료되지 않은 Access Token입니다."),
    FILE_EXTENSION_INVALID(HttpStatus.BAD_REQUEST, "FILE_EXTENSION_INVALID", "지원하지 않는 파일 포맷입니다."),
    IMAGES_LIMIT_EXCEEDED(HttpStatus.BAD_REQUEST, "IMAGES_LIMIT_EXCEEDED", "등록 가능한 이미지 갯수를 초과했습니다."),
    KEYWORDS_LIMIT_EXCEEDED(HttpStatus.BAD_REQUEST, "KEYWORDS_LIMIT_EXCEEDED", "등록 가능한 키워드 갯수를 초과했습니다."),
    KEYWORDS_LENGTH_LIMIT_EXCEEDED(HttpStatus.BAD_REQUEST, "KEYWORDS_LENGTH_LIMIT_EXCEEDED", "키워드의 글자 수를 초과했습니다."),
    ALREADY_EXIST_FRIEND_REQUEST(HttpStatus.BAD_REQUEST, "ALREADY_EXIST_FRIEND_REQUEST", "이미 존재하는 친구 요청입니다."),
    SELF_FRIEND_REQUEST(HttpStatus.BAD_REQUEST, "SELF_FRIEND_REQUEST", "자기 자신에게 친구 요청을 보낼 수 없습니다."),
    ALREADY_PROCESSED_FRIEND_REQUEST(HttpStatus.BAD_REQUEST, "ALREADY_PROCESSED_FRIEND_REQUEST", "이미 처리된 친구 요청입니다."),

    /* 401 */
    ACCESS_DENIED(HttpStatus.UNAUTHORIZED, "ACCESS_DENIED", "유효한 인증 정보가 아닙니다."),
    EXPIRED_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "EXPIRED_ACCESS_TOKEN", "Access Token이 만료되었습니다. 토큰을 재발급해주세요"),

    /* 403 */
    FORBIDDEN(HttpStatus.FORBIDDEN, "FORBIDDEN", "접근할 수 있는 권한이 없습니다."),
    EXPIRED_OR_PREVIOUS_REFRESH_TOKEN(HttpStatus.FORBIDDEN, "EXPIRED_OR_PREVIOUS_REFRESH_TOKEN", "만료되었거나 이전에 발급된 Refresh Token입니다."),
    FRIEND_REQUEST_RECEIVER_MISMATCH(HttpStatus.FORBIDDEN, "FRIEND_REQUEST_RECEIVER_MISMATCH", "현재 사용자가 친구 요청의 수신자가 아닙니다."),

    /* 404 */
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "MEMBER_NOT_FOUND", "존재하지 않는 유저입니다."),
    FRIEND_REQUEST_NOT_FOUND(HttpStatus.NOT_FOUND, "FRIEND_REQUEST_NOT_FOUND", "존재하지 않는 친구 요청입니다."),
    FRIENDSHIP_NOT_FOUND(HttpStatus.NOT_FOUND, "FRIENDSHIP_NOT_FOUND", "존재하지 않는 친구 관계입니다."),
    PLACE_NOT_FOUND(HttpStatus.NOT_FOUND, "PLACE_NOT_FOUND", "존재하지 않는 업체입니다."),
    REVIEW_NOT_FOUND(HttpStatus.NOT_FOUND, "REVIEW_NOT_FOUND", "존재하지 않는 리뷰입니다."),
    FRIEND_NOT_FOUND(HttpStatus.NOT_FOUND, "FRIEND_NOT_FOUND", "해당 이름을 가진 친구를 찾을 수 없습니다."),

    /* 500 */
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR", "예상치 못한 서버 에러가 발생했습니다."),
    SSE_CONNECTION_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "SSE_CONNECTION_ERROR", "SSE 연결 중 오류가 발생했습니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
