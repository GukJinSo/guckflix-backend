package guckflix.backend.dto.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
public class MemberForm {

    @NotBlank
    @Length(min = 8, max = 20)
    private String username;

    @NotBlank
    @Length(min = 8, max = 20)
    private String password;

    @NotNull
    @NotBlank
    private String email;
}
