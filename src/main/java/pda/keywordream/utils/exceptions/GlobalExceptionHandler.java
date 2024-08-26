package pda.keywordream.utils.exceptions;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver;
import pda.keywordream.utils.ApiUtils;
import pda.keywordream.utils.ApiUtils.ApiResult;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 지원하지 않은 HTTP method 호출 할 경우 발생
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public ApiResult handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e){
        return ApiUtils.error(e.getMethod(), HttpStatus.METHOD_NOT_ALLOWED);
    }

    /**
     * 필요한 Request body element을 입력하지 않을 경우
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResult handleHttpMessageNotReadableException(HttpMessageNotReadableException e){
        log.error("HttpMessageNotReadableException = {}", e.getMessage());
        return ApiUtils.error(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    /**
     * 필요한 Request body element을 입력하지 않을 경우
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResult handleMethodArgumentNotValidException(MethodArgumentNotValidException e){
        log.error("MethodArgumentNotValidException = {}", e.getMessage());
        Map<String, String> errorMap = new HashMap<>();

        e.getFieldErrors().forEach(fieldError -> {
            errorMap.put(fieldError.getField(), fieldError.getDefaultMessage());
        });

        return ApiUtils.error(errorMap, HttpStatus.BAD_REQUEST);
    }

    /*
     * DB에 해당 element가 없을 경우
     * */
    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiResult handleNoSuchElementException(NoSuchElementException e){
        log.error("NoSuchElementException = {}", e.getMessage());
        return ApiUtils.error(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    /*
     * request param 부족
     * */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResult handleMissingServletRequestParameterException(MissingServletRequestParameterException e){
        log.error("MissingServletRequestParameterException = {}", e.getMessage());
        String name = e.getParameterName();
        return ApiUtils.error(name + " is required", HttpStatus.BAD_REQUEST);
    }

    /*
     * Token 만료
     * */
    @ExceptionHandler(ExpiredJwtException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ApiResult handleExpiredJwtException(ExpiredJwtException e){
        log.error("ExpiredJwtException = {}", e.getMessage());
        return ApiUtils.error("Token has expired", HttpStatus.UNAUTHORIZED);
    }

    /*
     * 잘못된 Token일 경우
     * */
    @ExceptionHandler(SignatureException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ApiResult handleSignatureException(SignatureException e){
        log.error("JwtTokenExpiredException = {}", e.getMessage());
        return ApiUtils.error("Invalidated JWT", HttpStatus.UNAUTHORIZED);
    }

    /*
    * request header 넘겨주지 않았을 경우
    * */
    @ExceptionHandler(MissingRequestHeaderException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResult handleMissingRequestHeaderException(MissingRequestHeaderException e){
        log.error("MissingRequestHeaderException = {}", e.getMessage());
        return ApiUtils.error(e.getHeaderName()+" is required", HttpStatus.BAD_REQUEST);
    }

    /*
    * 서버에서 처리하지 못한 예기치 않은 오류가 발생할 경우
    * */
    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiResult handleRuntimeException(RuntimeException e){
        log.error("RuntimeException = {}", e.getMessage());
        return ApiUtils.error(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
