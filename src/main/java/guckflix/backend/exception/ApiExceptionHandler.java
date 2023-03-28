package guckflix.backend.exception;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Locale;

/**
 * ErrorDto : 상태코드(400), 상태코드 메세지(BAD_REQUEST), 메세지("No movie of given id")를 출력하기 위한 클래스
 * BindErrorDto : ErrorDto에 더해 바인딩 에러를 locale(ko or en) 따라 출력하기 위해서 만든 클래스
 * BindingErrorDetail : 필드 에러와 오브젝트 에러 관계없이 받아서 일반적인 메세지로 만드는 클래스
 * 에러 형태를 스프링 기본 값 대신 원하는 대로 만들기 위해 사용
 */

@RestControllerAdvice
@Slf4j
@RequiredArgsConstructor
public class ApiExceptionHandler {

    private final MessageSource messageSource;

    @ExceptionHandler(value = MovieNotFoundException.class)
    public ResponseEntity<ErrorDto> movieNotFound(MovieNotFoundException e) {
        log.warn("RuntimeMovieNotFoundException", e);
        ErrorDto errorResponse = new ErrorDto(HttpStatus.BAD_REQUEST.value(),HttpStatus.BAD_REQUEST, e.getMessage());
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(value = NotAllowedIdException.class)
    public ResponseEntity<ErrorDto> notAllowedIdException(NotAllowedIdException e) {
        log.warn("NotAllowedIdException", e);
        ErrorDto errorResponse = new ErrorDto(HttpStatus.BAD_REQUEST.value(),HttpStatus.BAD_REQUEST, e.getMessage());
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(value = MemberDuplicateException.class)
    public ResponseEntity<ErrorDto> memberDuplicateException(MemberDuplicateException e) {
        log.warn("MemberDuplicateException", e);
        ErrorDto errorResponse = new ErrorDto(HttpStatus.CONFLICT.value(),HttpStatus.CONFLICT, e.getMessage());
        return ResponseEntity.badRequest().body(errorResponse);
    }

    /**
     * 요청 Locale에 따라 BindingResult 응답 메세지 변경
     */
    @ExceptionHandler(value = BindException.class)
    public ResponseEntity<BindErrorDto> bindException(BindException e, Locale locale) {
        String message = locale.getLanguage().equals("en") ? "validation fail" : "잘못된 요청입니다";
        log.info("BindException", e);
        BindErrorDto bindErrorDto = new BindErrorDto(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST, message, e.getFieldErrors(), e.getAllErrors(), messageSource, locale);
        return ResponseEntity.badRequest().body(bindErrorDto);
    }


}
