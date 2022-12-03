package guckflix.backend.dto.response;

import guckflix.backend.dto.MovieDto;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.util.List;

@Getter
@Setter
public class MovieResponse {

    private int page;
    List<MovieDto> results;

    public MovieResponse(int page, List<MovieDto> results) {
        this.page = page;
        this.results = results;
    }
}
