package guckflix.backend.entity;


import guckflix.backend.entity.enums.MemberRole;
import lombok.*;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@Setter
@EqualsAndHashCode
public class Member {

    @Id
    @GeneratedValue private Long id;

    private String username;

    private String password;

    private String email;

    @Column(name = "member_role")
    @Enumerated(EnumType.STRING)
    private MemberRole role;

}
