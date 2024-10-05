package com.ceos20.instagram.global.exception;

import org.springframework.http.HttpStatus;

public class ExceptionResponse {
    private final HttpStatus httpStatus;
    private final String divisionCode;
    private final String message;

    public ExceptionResponse(HttpStatus httpStatus, String divisionCode, String message) {
        this.httpStatus = httpStatus;
        this.divisionCode = divisionCode;
        this.message = message;
    }

    //NotFound Exception 응답
    public static ExceptionResponse from(NotFoundException exception) {
        ExceptionCode code=exception.getExceptionCode();
        return new ExceptionResponse(code.getStatus(), code.getDivisionCode(), exception.getMessage());
    }

    // ForbiddenException 응답
    public static ExceptionResponse from(ForbiddenException exception) {
        ExceptionCode code=exception.getExceptionCode();
        return new ExceptionResponse(code.getStatus(), code.getDivisionCode(), exception.getMessage());
    }
}
