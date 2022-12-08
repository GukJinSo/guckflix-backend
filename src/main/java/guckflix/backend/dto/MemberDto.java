package guckflix.backend.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
public class MemberDto {

    private String username;

    private String password;

    private String email;

    @Getter
    @Setter
    @ToString
    public static class Post{

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

    @Getter
    @Setter
    @ToString
    public static class PasswordChangePatch {

        @NotBlank
        @Length(min = 8, max = 20)
        private String password;

    }

}
