package guckflix.backend.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import guckflix.backend.config.GenreCached;
import guckflix.backend.entity.Movie;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
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

    @JsonIgnore
    public MovieDto(Movie entity){
        this.setId(entity.getId());
        this.setGenres(genreMatch(entity.getGenres()));
        this.setOverview(entity.getOverview());
        this.setPopularity(entity.getPopularity());
        this.setTitle(entity.getTitle());
        this.setPosterPath(entity.getPosterPath());
        this.setBackdropPath(entity.getBackdropPath());
        this.setVoteAverage(entity.getVoteAverage());
        this.setVoteCount(entity.getVoteCount());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        this.setReleaseDate(dateFormat.format(entity.getReleaseDate()));
    }

    @JsonIgnore
    private List<Map.Entry<Long, String>> genreMatch(String entityGenres){
        Map<Long, String> genreMap = new HashMap<>();
        List<String> genreList = Arrays.asList(entityGenres.split(","));
        for (String genre : genreList) {
            long genreId = Long.parseLong(genre);
            genreMap.put(genreId, GenreCached.getGenres().get(genreId));
        }
        List<Map.Entry<Long, String>> collect = genreMap.entrySet().stream().collect(Collectors.toList());
        return collect;
    }


}
