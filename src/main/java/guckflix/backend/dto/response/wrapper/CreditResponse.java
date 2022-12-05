package guckflix.backend.dto.response.wrapper;

import com.fasterxml.jackson.annotation.JsonProperty;
import guckflix.backend.dto.response.CreditDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CreditResponse {

    @JsonProperty("movie_id")
    private Long movieId;

    private List<CreditDto> results;

    public CreditResponse(Long movieId, List<CreditDto> results) {
        this.movieId = movieId;
        this.results = results;

    }
}

