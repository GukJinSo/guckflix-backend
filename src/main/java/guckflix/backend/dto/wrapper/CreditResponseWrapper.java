package guckflix.backend.dto.wrapper;

import com.fasterxml.jackson.annotation.JsonProperty;
import guckflix.backend.dto.CreditDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CreditResponseWrapper {

    @JsonProperty("movie_id")
    private Long movieId;

    private List<CreditDto.Response> results;

    public CreditResponseWrapper(Long movieId, List<CreditDto.Response> results) {
        this.movieId = movieId;
        this.results = results;

    }
}

