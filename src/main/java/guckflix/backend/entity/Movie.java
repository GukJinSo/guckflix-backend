package guckflix.backend.entity;

import guckflix.backend.config.GenreCached;
import guckflix.backend.dto.MovieDto;
import lombok.*;

import javax.persistence.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@EqualsAndHashCode
public class Movie {

    @Id @GeneratedValue private Long id;

    private String title;

    @Column(length = 1000)
    private String overview;

    private LocalDate releaseDate;

    private String genres;

    private float popularity;

    private int voteCount;

    private float voteAverage;

    private String backdropPath;

    private String posterPath;

    @OneToMany(mappedBy = "movie")
    public List<Credit> credits = new ArrayList<>();

    @OneToMany(mappedBy = "movie")
    public List<Video> videos = new ArrayList<>();

    public void updateVoteAdd(float voteRating) {
        this.voteAverage = ((this.voteAverage * voteCount) + voteRating) / (voteCount + 1);
        this.voteCount += 1;
    }

    public void updateVoteDelete(float voteRating) {
        this.voteAverage = ((this.voteAverage * voteCount) - voteRating) / (voteCount - 1);
        this.voteCount -= 1;
    }

    public void updateDetail(MovieDto.Update form, List<Credit> credits) {
        this.title = form.getTitle();
        this.backdropPath = form.getBackdropPath();
        this.genres = GenreCached.genreToString(form.getGenres());
        this.overview = form.getOverview();
        this.releaseDate = form.getReleaseDate();

        this.credits = credits;
        for (Credit credit : credits) {
            credit.changeMovie(this);
        }
    }
}