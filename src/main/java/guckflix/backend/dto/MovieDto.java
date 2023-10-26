package guckflix.backend.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import guckflix.backend.config.GenreCached;
import guckflix.backend.entity.Movie;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.*;

public class MovieDto {


    @Getter
    @Setter
    @NoArgsConstructor
    @ApiModel(value = "MovieDto-Post")
    public static class Post {

        @NotBlank
        private String title;

        @NotBlank
        private String overview;

        @Valid
        @NotNull
        @Size(min = 1)
        private List<GenreDto> genres;

        @NotNull
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        @JsonProperty("release_date")
        private LocalDate releaseDate;

        @JsonProperty("backdrop_path")
        private String backdropPath;

        @JsonProperty("poster_path")
        private String posterPath;

        @Valid
        @NotNull
        @Size(min = 1)
        private List<CreditDto.Post> credits;

    }

    @Getter
    @Setter
    @NoArgsConstructor
    @ApiModel(value = "MovieDto-Response")
    public static class Response {
        private Long id;

        private String title;

        private String overview;

        private float popularity;

        @JsonProperty("vote_count")
        private int voteCount;

        @JsonProperty("vote_average")
        private float voteAverage;

        private List<GenreDto> genres;

        @JsonProperty("release_date")
        private LocalDate releaseDate;

        @JsonProperty("backdrop_path")
        private String backdropPath;

        @JsonProperty("poster_path")
        private String posterPath;

        @JsonIgnore
        public Response(Movie entity){
            this.setId(entity.getId());
            this.setGenres(GenreCached.genreStringToList(entity.getGenres()));
            this.setOverview(entity.getOverview());
            this.setPopularity(entity.getPopularity());
            this.setTitle(entity.getTitle());
            this.setPosterPath(entity.getPosterPath());
            this.setBackdropPath(entity.getBackdropPath());
            this.setVoteAverage(entity.getVoteAverage());
            this.setVoteCount(entity.getVoteCount());
            this.setReleaseDate(entity.getReleaseDate());
        }


    }

    @Getter
    @Setter
    @NoArgsConstructor
    @ApiModel(value = "MovieDto-Response")
    public static class Update {

        private Long id;

        private String title;

        private String overview;

        private List<GenreDto> genres;

        @JsonProperty("release_date")
        private LocalDate releaseDate;

        @JsonProperty("backdrop_path")
        private String backdropPath;

        @JsonProperty("poster_path")
        private String posterPath;

    }





}
