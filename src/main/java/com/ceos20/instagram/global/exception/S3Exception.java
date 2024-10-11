package com.ceos20.instagram.global.exception;

public class S3Exception extends RuntimeException {
    private final ExceptionCode exceptionCode;

    public S3Exception(final ExceptionCode exceptionCode) {
        super(exceptionCode.getMessage());
        this.exceptionCode = exceptionCode;
    }

    public ExceptionCode getExceptionCode() {
        return exceptionCode;
    }
}
