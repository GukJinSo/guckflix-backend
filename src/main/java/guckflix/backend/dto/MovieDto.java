package guckflix.backend.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
public class MovieDto {

    private Long id;

    private String releaseDate;

    private float popularity;

    private String backdropPath;

    private String posterPath;

    private String overview;

    private String title;

    private float voteCount;

    private float voteAverage;

    private List<Map.Entry<Long, String>> genres;

}
