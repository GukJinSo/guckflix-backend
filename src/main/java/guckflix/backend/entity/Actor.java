package guckflix.backend.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@EqualsAndHashCode
public class Actor {

    @Column(name = "actor_id")
    @Id @GeneratedValue private Long id;

    private String overview;

    @Column(name = "birth_day")
    private LocalDate birthDay;

    private String name;

    private String profilePath;

    private float popularity;

    @OneToMany
    @JoinColumn(name = "actor_id")
    private List<Credit> credits;
}
