package guckflix.backend.entity;

import guckflix.backend.dto.ActorDto;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
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

    private String biography;

    private LocalDate birthDay;

    private LocalDate deathDay;

    private String placeOfBirth;

    private String name;

    private String profilePath;

    private float popularity;

    @OneToMany(mappedBy = "actor")
    private List<Credit> credits = new ArrayList<>();

    public void updateDetail(ActorDto.Update actorUpdafeForm) {
        name = actorUpdafeForm.getName();
        profilePath = actorUpdafeForm.getProfilePath();
        birthDay = actorUpdafeForm.getBirthDay();
        biography = actorUpdafeForm.getBiography();
    }


}

