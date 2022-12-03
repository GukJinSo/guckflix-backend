package guckflix.backend.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
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
    private List<Credit> credits = new ArrayList<>();

}
