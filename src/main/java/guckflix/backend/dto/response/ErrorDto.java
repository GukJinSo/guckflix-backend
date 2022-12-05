package guckflix.backend.dto.response;

import lombok.Getter;

@Getter
public class ErrorDto {

    private String message;

    public ErrorDto(String message) {
        this.message = message;
    }
}
