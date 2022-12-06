package guckflix.backend.exception;


import guckflix.backend.dto.ErrorDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class CustomExceptionHandler {

    @ExceptionHandler(value = RuntimeMovieNotFoundException.class)
    public ResponseEntity<ErrorDto> movieNotFound(RuntimeMovieNotFoundException e) {
        log.warn("Exception", e);
        ErrorDto errorResponse = new ErrorDto(HttpStatus.BAD_REQUEST.value(),HttpStatus.BAD_REQUEST, e.getMessage());
        return ResponseEntity.badRequest().body(errorResponse);
    }
}
