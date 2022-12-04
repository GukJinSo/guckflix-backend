package guckflix.backend.dto.response.wrapper;

import guckflix.backend.dto.response.MovieDto;
import lombok.Getter;
import lombok.Setter;

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
