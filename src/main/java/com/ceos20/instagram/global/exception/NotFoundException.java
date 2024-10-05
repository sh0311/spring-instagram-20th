package com.ceos20.instagram.global.exception;


// 특정 리소스를 못 찾았을 때 발생ㅎ는 예외를 처리하기 위한 커스텀 예외클래스
public class NotFoundException extends RuntimeException{

    private final ExceptionCode exceptionCode;

    public NotFoundException(final ExceptionCode exceptionCode) {
        super(exceptionCode.getMessage());
        this.exceptionCode = exceptionCode;
    }

    public ExceptionCode getExceptionCode() {
        return exceptionCode;
    }
}
