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

@RestControllerAdvice
@Slf4j
@RequiredArgsConstructor
public class ApiExceptioHandler {

    private final MessageSource messageSource;

    @ExceptionHandler(value = RuntimeMovieNotFoundException.class)
    public ResponseEntity<ErrorDto> movieNotFound(RuntimeMovieNotFoundException e) {
        log.warn("Exception", e);
        ErrorDto errorResponse = new ErrorDto(HttpStatus.BAD_REQUEST.value(),HttpStatus.BAD_REQUEST, e.getMessage());
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
