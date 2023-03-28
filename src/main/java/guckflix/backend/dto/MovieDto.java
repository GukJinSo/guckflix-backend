package guckflix.backend.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import guckflix.backend.config.GenreCached;
import guckflix.backend.entity.Movie;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class MovieDto {


    @Getter
    @Setter
    @NoArgsConstructor
    @ApiModel(value = "MovieDto-Post")
    public static class Post {

        private String title;

        private String overview;

        private List<Map.Entry<Long, String>> genres;

        @JsonProperty("release_date")
        private LocalDate releaseDate;

        private MultipartFile w500FIle;

        private MultipartFile originFile;

        @JsonProperty("backdrop_path")
        private String backdropPath;

        @JsonProperty("poster_path")
        private String posterPath;

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

        private List<Map.Entry<Long, String>> genres;

        @JsonProperty("release_date")
        private LocalDate releaseDate;

        @JsonProperty("backdrop_path")
        private String backdropPath;

        @JsonProperty("poster_path")
        private String posterPath;

        @JsonIgnore
        public Response(Movie entity){
            this.setId(entity.getId());
            this.setGenres(genreToListEntry(entity.getGenres()));
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

        private List<Map.Entry<Long, String>> genres;

        @JsonProperty("release_date")
        private LocalDate releaseDate;

        @JsonProperty("backdrop_path")
        private String backdropPath;

        @JsonProperty("poster_path")
        private String posterPath;

    }


    @JsonIgnore
    public static List<Map.Entry<Long, String>> genreToListEntry(String entityGenres){

        if(entityGenres == null || entityGenres.equals("")){
            return null;
        }

        Map<Long, String> genreMap = new HashMap<>();
        List<String> genreList = Arrays.asList(entityGenres.split(","));
        for (String genre : genreList) {
            long genreId = Long.parseLong(genre);
            genreMap.put(genreId, GenreCached.getGenres().get(genreId));
        }
        List<Map.Entry<Long, String>> collect = genreMap.entrySet().stream().collect(Collectors.toList());
        return collect;
    }

    @JsonIgnore
    public static String genreToString(List<Map.Entry<Long, String>> genres){

        if(genres == null || genres.size() == 0) {
            return null;
        }

        return genres.stream().map((entry) -> Integer.toString(entry.getKey().intValue()))
                .collect(Collectors.toList())
                .stream().collect(Collectors.joining(","));
    }


}
