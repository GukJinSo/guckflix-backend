package guckflix.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@EqualsAndHashCode
public class Movie {

    @Id private Long id;

    private String title;

    @Column(length = 1000)
    private String overview;

    private Date releaseDate;

    private String genres;

    private float popularity;

    private float voteCount;

    private float voteAverage;

    private String backdropPath;

    private String posterPath;

    @OneToMany(mappedBy = "movie")
    public List<Credit> credits = new ArrayList<>();

    @OneToMany(mappedBy = "movie")
    public List<Video> videos = new ArrayList<>();

}
