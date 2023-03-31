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

    public void changeActor(Actor actor){
        this.actor = actor;
        actor.getCredits().add(this);
    }

    public void update(int order, String casting) {
        this.order = order;
        this.casting = casting;
    }

    public void changeMovie(Movie movie) {
        if(this.movie != null){
            this.movie.getCredits().remove(this);
        }
        this.movie = movie;
        movie.getCredits().add(this);
    }

    public void removeRelationWithMovie(){
        this.movie.getCredits().remove(this);
        this.movie = null;
    }

    public void removeRelationWithActor() {
        this.actor.getCredits().remove(this);
        this.actor = null;
    }
}
