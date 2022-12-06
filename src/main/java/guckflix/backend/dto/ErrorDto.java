package guckflix.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ErrorDto {

    @JsonProperty("status_code")
    private int statusCode;

    private HttpStatus status;

    private String message;

    public ErrorDto(int statusCode, HttpStatus status, String message) {
        this.statusCode = statusCode;
        this.status = status;
        this.message = message;
    }
}
