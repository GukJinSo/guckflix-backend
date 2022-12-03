package guckflix.backend.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Credit {

    @Id @GeneratedValue @Column(name = "credit_id")
    private Long id;

    @Column(length = 500)
    private String casting;

    private Integer castingOrder;

    @JoinColumn(name = "movie_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Movie movie;

    @JoinColumn(name = "actor_id")
    private Long actorId;

}
