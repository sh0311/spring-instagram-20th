package com.ceos20.instagram.global.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    //NotFound Exception
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleNotFoundException(NotFoundException e){
        log.error(e.getMessage(),e);  //모든 예외 클래스는 Throwable 클래스를 상속받는다. Throwable 클래스에는 getMessage()라는 메서드가 이미 정의되어있다. 이 메서드는 예외가 발생할 때 생성자에서 전달된 예외 메시지를 반환하는 역할을 하기에 NotFoundException에는 @Geter가 없어도 getMessage() 사용가능 함.
        final ExceptionResponse response=ExceptionResponse.from(e);
        return ResponseEntity.status(NOT_FOUND).body(response);
    }

    //ForbiddenException
    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ExceptionResponse> handleForbiddenException(ForbiddenException e){
        log.error(e.getMessage(),e);
        final ExceptionResponse response=ExceptionResponse.from(e);
        return ResponseEntity.status(FORBIDDEN).body(response);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ExceptionResponse> handleBadRequestException(BadRequestException e){
        log.error(e.getMessage(),e);
        final ExceptionResponse response=ExceptionResponse.from(e);
        return ResponseEntity.status(BAD_REQUEST).body(response);

    }

    //스프링에서 제공하는 예외클래스이므로 커스텀 클래스를 만들 필요가 없다
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleMethodArgumentNotValidException(MethodArgumentNotValidException e){
        log.error(e.getMessage(),e);
        return ResponseEntity.status(BAD_REQUEST).body(e.getBindingResult().getFieldErrors().get(0).getDefaultMessage());  //유효성 검사 실패 시 첫 번째 필드 에러 메시지만을 응답 바디로 반환


    }
}
