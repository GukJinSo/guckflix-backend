package guckflix.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import guckflix.backend.entity.enums.ISO3166;
import guckflix.backend.entity.enums.ISO639;
import guckflix.backend.entity.enums.VideoProvider;
import guckflix.backend.entity.enums.VideoType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class VideoDto {

    @JsonProperty("movie_id")
    private Long movieId;

    @JsonProperty("video_name")
    private String name;

    @JsonProperty("iso_639")
    private ISO639 iso639;

    @JsonProperty("iso_3166")
    private ISO3166 iso3166;

    private String key;

    private Boolean official;

    private VideoProvider site;

    private VideoType type;

    private LocalDateTime publishedAt;
}
