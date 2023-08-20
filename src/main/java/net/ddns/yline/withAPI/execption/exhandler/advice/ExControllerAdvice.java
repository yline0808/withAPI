package net.ddns.yline.withAPI.execption.exhandler.advice;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import net.ddns.yline.withAPI.execption.exType.UserException;
import net.ddns.yline.withAPI.execption.exhandler.ErrorResultList;
import net.ddns.yline.withAPI.execption.exhandler.ErrorResult;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

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
        log.error("[IllegalArgumentException]", e);
        return new ErrorResult("Bad Request", e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorResultList methodArgumentNotValidHandler(MethodArgumentNotValidException e) {
        log.error("[MethodArgumentNotValidException]", e);

        List<String> errMsg = new ArrayList<>();

        e.getBindingResult().getAllErrors()
                .forEach(err -> errMsg.add(err.getDefaultMessage()));
        return new ErrorResultList("Bad Request", errMsg);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResult> messageNotReadableHandler(HttpMessageNotReadableException e) {
        log.error("[HttpMessageNotReadableException]",e);
        ErrorResult errorResult = new ErrorResult("User error", e.getMessage());
        return new ResponseEntity<>(errorResult, HttpStatus.BAD_REQUEST);
    }

    /**
     * id, pw 틀렸을 경우
     * @param e
     * @return
     */
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResult> badCredentialHandler(BadCredentialsException e) {
        log.error("[BadCredentialsException]", e);
        ErrorResult errorResult = new ErrorResult("email or password error", e.getMessage());
        return new ResponseEntity<>(errorResult, HttpStatus.BAD_REQUEST);
    }

    /**
     * token 만료
     * @param e
     * @return
     */
    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ErrorResult> expiredJwtHandler(ExpiredJwtException e) {
        log.error("[ExpiredJwtException]", e);
        ErrorResult errorResult = new ErrorResult("token expired error", e.getMessage());
        return new ResponseEntity<>(errorResult, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(SignatureException.class)
    public ResponseEntity<ErrorResult> signatureHandler(SignatureException e) {
        log.error("[SignatureException]", e);
        ErrorResult errorResult = new ErrorResult("token signature error", e.getMessage());
        return new ResponseEntity<>(errorResult, HttpStatus.BAD_REQUEST);
    }

    // @ExceptionHandler 가 붙은 메서드의 인수에 예외 지정
    @ExceptionHandler
    public ResponseEntity<ErrorResult> userExHandler(UserException e) {
        log.error("[exceptionHandler] ex", e);
        ErrorResult errorResult = new ErrorResult("USER-EX", e.getMessage());
        return new ResponseEntity<>(errorResult, HttpStatus.BAD_REQUEST);
    }

    // @ExceptionHandler 가 붙은 메서드의 인수에 예외 지정
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler
    public ErrorResult exHandler(Exception e) {
        log.error("[exceptionHandler] ex", e);
        return new ErrorResult("EX", "내부 오류");
    }
}
