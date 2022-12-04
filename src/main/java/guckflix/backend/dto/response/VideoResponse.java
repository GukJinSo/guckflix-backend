package guckflix.backend.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import guckflix.backend.dto.VideoDto;
import guckflix.backend.entity.enums.ISO3166;
import guckflix.backend.entity.enums.ISO639;
import guckflix.backend.entity.enums.VideoProvider;
import guckflix.backend.entity.enums.VideoType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import java.time.LocalDateTime;
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
