package guckflix.backend.dto.wrapper;

import com.fasterxml.jackson.annotation.JsonProperty;
import guckflix.backend.dto.VideoDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class VideoResponseWrapper {

    @JsonProperty("movie_id")
    private Long movieId;

    private List<VideoDto.Response> results;

    public VideoResponseWrapper(Long movieId, List<VideoDto.Response> results) {
        this.movieId = movieId;
        this.results = results;
    }
}
