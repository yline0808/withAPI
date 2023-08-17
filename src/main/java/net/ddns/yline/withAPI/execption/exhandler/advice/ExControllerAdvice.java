package net.ddns.yline.withAPI.execption.exhandler.advice;

import lombok.extern.slf4j.Slf4j;
import net.ddns.yline.withAPI.execption.exType.UserException;
import net.ddns.yline.withAPI.execption.exhandler.ErrorResult;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestControllerAdvice(basePackages = "net.ddns.yline.withAPI.controller")
public class ExControllerAdvice {
    // @ExceptionHandler() 내부에 예외 지정
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public ErrorResult illegalExHandler(IllegalArgumentException e) {
        log.error("[exceptionHandler] ex", e);
        return new ErrorResult("Bad Request", e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorResultList methodArgumentNotValidHandler(MethodArgumentNotValidException e) {
        log.error("[exceptionHandler] ex", e);
        Map<Integer, String> errMap = new HashMap<>();
        e.getBindingResult().getAllErrors()
                .forEach(err -> errMap.put(errMap.size(), err.getDefaultMessage()));
        return new ErrorResultList("Bad Request", errMap);
//        return new ErrorResult("BAD", e.getBindingResult().getAllErrors().toString());
    }

    // @ExceptionHandler 가 붙은 메서드의 인수에 예외 지정
    @ExceptionHandler
    public ResponseEntity<ErrorResult> userExHandler(UserException e) {
        log.error("[exceptionHandler] ex", e);
        ErrorResult errorResult = new ErrorResult("USER-EX", e.getMessage());
        return new ResponseEntity(errorResult, HttpStatus.BAD_REQUEST);
    }

    // @ExceptionHandler 가 붙은 메서드의 인수에 예외 지정
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler
    public ErrorResult exHandler(Exception e) {
        log.error("[exceptionHandler] ex", e);
        return new ErrorResult("EX", "내부 오류");
    }

}
