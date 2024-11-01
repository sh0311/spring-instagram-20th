package com.ceos20.instagram.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.lang.reflect.Method;
import java.nio.file.AccessDeniedException;

@Getter  //GlobalExceptionHandler에서 ExceptionResponse 객체를 ResponseEntity의 body로 전달하면, 스프링이 해당 객체를 JSON으로 변환해 클라이언트에 반환하는데 이때 @Getter가 필요(ExceptionResponse의 각 필드 값에 접근해 JSON 객체를 만들기 때문에 필요)
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

    //BadRequestException 응답
    public static ExceptionResponse from(BadRequestException exception) {
        ExceptionCode code=exception.getExceptionCode();
        return new ExceptionResponse(code.getStatus(), code.getDivisionCode(), exception.getMessage());
    }

    //S3Exception 응답
    public static ExceptionResponse from(S3Exception exception) {
        ExceptionCode code=exception.getExceptionCode();
        return new ExceptionResponse(code.getStatus(), code.getDivisionCode(), exception.getMessage());
    }

}
