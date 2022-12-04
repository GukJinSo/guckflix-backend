package guckflix.backend.exception;


import guckflix.backend.dto.response.wrapper.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class CustomExceptionHandler {

    @ExceptionHandler(value = RuntimeMovieNotFoundException.class)
    public ResponseEntity<ErrorResponse> movieNotFound(RuntimeMovieNotFoundException e) {
        log.warn("Exception", e);
        ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
        return ResponseEntity.badRequest().body(errorResponse);
    }
}
