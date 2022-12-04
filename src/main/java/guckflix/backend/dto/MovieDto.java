package guckflix.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
public class MovieDto {

    private Long id;

    private String title;

    private String overview;

    private float popularity;

    @JsonProperty("vote_count")
    private float voteCount;

    @JsonProperty("vote_average")
    private float voteAverage;

    private List<Map.Entry<Long, String>> genres;

    @JsonProperty("release_date")
    private String releaseDate;

    @JsonProperty("backdrop_path")
    private String backdropPath;

    @JsonProperty("poster_path")
    private String posterPath;

}
