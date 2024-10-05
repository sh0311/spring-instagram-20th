package com.ceos20.instagram.global.exception;

import java.lang.reflect.Executable;

public class ForbiddenException extends RuntimeException {
    private final ExceptionCode exceptionCode;

    public ForbiddenException(final ExceptionCode exceptionCode) {
        super(exceptionCode.getMessage());
        this.exceptionCode = exceptionCode;
    }

    public ExceptionCode getExceptionCode() {
        return exceptionCode;
    }
}
