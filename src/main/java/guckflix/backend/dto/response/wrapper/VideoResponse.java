package guckflix.backend.dto.response.wrapper;

import com.fasterxml.jackson.annotation.JsonProperty;
import guckflix.backend.dto.response.VideoDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class VideoResponse {

    @JsonProperty("movie_id")
    private Long movieId;

    private List<VideoDto> results;

    public VideoResponse(Long movieId, List<VideoDto> results) {
        this.movieId = movieId;
        this.results = results;
    }
}
