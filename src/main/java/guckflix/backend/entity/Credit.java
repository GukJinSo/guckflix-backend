package guckflix.backend.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode
public class Credit {

    @Id @GeneratedValue @Column(name = "credit_id")
    private Long id;

    @Column(length = 500)
    private String casting;

    @Column(name = "casting_order")
    private Integer order;

    @JoinColumn(name = "movie_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Movie movie;

    @JoinColumn(name = "actor_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Actor actor;

}
