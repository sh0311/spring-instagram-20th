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

    ALREADY_EXIST_COMMENT_LIKE(HttpStatus.BAD_REQUEST, "B001","이미 좋아요를 누른 댓글입니다.");

    private final HttpStatus status;
    private final String divisionCode;
    private final String message;

    ExceptionCode(final HttpStatus status, final String divisionCode, final String message) {
        this.status = status;
        this.divisionCode = divisionCode;
        this.message = message;
    }

}
