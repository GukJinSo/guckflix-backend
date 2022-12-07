package guckflix.backend.entity;


import guckflix.backend.entity.base.TimeDateBaseEntity;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@EqualsAndHashCode
public class Review extends TimeDateBaseEntity {

    @Column(name = "review_id")
    @Id @GeneratedValue private Long id;

    @Column(name = "movie_id")
    private Long movieId;

    @Column(name = "user_id")
    private Long userId;

    private String content;

    @Column(name = "vote_rating")
    private Float voteRating;

}
