package com.ceos20.instagram.global.exception;

public class BadRequestException extends RuntimeException {
    private final ExceptionCode exceptionCode;

    public BadRequestException(ExceptionCode code) {
        super(code.getMessage());
        this.exceptionCode = code;
    }

    public ExceptionCode getExceptionCode() {
        return exceptionCode;
    }
}
