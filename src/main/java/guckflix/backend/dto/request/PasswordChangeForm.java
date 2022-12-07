package guckflix.backend.dto.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@ToString
public class PasswordChangeForm {

    @NotBlank
    @Length(min = 8, max = 20)
    private String password;

}
