package guckflix.backend.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@EqualsAndHashCode
public class Actor {

    @Column(name = "actor_id")
    @Id private Long id;

    private String name;

    private String profilePath;

    private float popularity;

    @OneToMany
    @JoinColumn(name = "actor_id")
    private List<Credit> credits;
}
