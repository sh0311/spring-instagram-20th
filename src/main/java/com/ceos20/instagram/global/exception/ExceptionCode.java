package com.ceos20.instagram.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ExceptionCode {
    NOT_FOUND_USER(HttpStatus.NOT_FOUND, "N001", "해당 id의 유저는 존재하지 않습니다."),
    NOT_FOUND_POST(HttpStatus.NOT_FOUND, "N002", "해당 id의 게시글은 존재하지 않습니다."),
    NOT_FOUND_FOLLOW(HttpStatus.NOT_FOUND, "N003", "해당 팔로우 객체는 존재하지 않습니다."),
    NOT_FOUND_ROOM(HttpStatus.NOT_FOUND, "N004", "해당 id의 채팅방은 존재하지 않습니다."),
    NOT_FOUND_USER_IN_ROOM(HttpStatus.NOT_FOUND, "N005", "해당 id의 유저가 해당 채팅방에 존재하지 않습니다."),
    NOT_FOUND_MESSAGE(HttpStatus.NOT_FOUND, "N006", "해당 id의 메시지는 존재하지 않습니다."),
    NOT_FOUND_COMMENT(HttpStatus.NOT_FOUND, "N007", "해당 id의 댓글은 존재하지 않습니다."),
    NOT_FOUND_PARENT_COMMENT(HttpStatus.NOT_FOUND, "N008", "해당 id의 부모댓글은 존재하지 않습니다."),
    NOT_FOUND_POST_LIKE(HttpStatus.NOT_FOUND, "N009", "해당 게시글 좋아요는 존재하지 않습니다."),
    NOT_FOUND_COMMENT_LIKE(HttpStatus.NOT_FOUND, "N010", "해당 댓글 좋아요는 존재하지 않습니다."),

    NOT_POST_OWNER(HttpStatus.FORBIDDEN, "F001", "게시글 작성자가 아닙니다."),

    ALREADY_EXIST_COMMENT_LIKE(HttpStatus.BAD_REQUEST, "B001","이미 좋아요를 누른 댓글입니다."),
    ALREADY_EXIST_NICKNAME(HttpStatus.BAD_REQUEST, "B002", "이미 존재하는 닉네임입니다."),
    ALREADY_EXIST_EMAIL(HttpStatus.BAD_REQUEST, "B003", "이미 존재하는 이메일입니다."),
    INVALID_EXTENSION(HttpStatus.BAD_REQUEST, "B004", "이미지 파일확장자 명이 아닙니다."),
    NO_FILENAME(HttpStatus.BAD_REQUEST, "B005", "파일의 이름이 존재하지 않습니다."),
    NO_REFRESH_TOKEN(HttpStatus.BAD_REQUEST, "B006","refresh token이 쿠키에 존재하지 않습니다"),
    EXPIRED_REFRESH_TOKEN(HttpStatus.BAD_REQUEST, "B007","해당 refresh token의 유효기간이 지났습니다"),
    INVALID_REFRESH_TOKEN(HttpStatus.BAD_REQUEST, "B008", "해당 토큰은 refresh 토큰이 아닙니다.");

    private final HttpStatus status;
    private final String divisionCode;
    private final String message;

    ExceptionCode(final HttpStatus status, final String divisionCode, final String message) {
        this.status = status;
        this.divisionCode = divisionCode;
        this.message = message;
    }

}
