package guckflix.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@ToString
public class MemberDto {


    @Getter
    @Setter
    @ToString
    @ApiModel(value = "MemberDto-Post")
    public static class Post{

        @NotBlank
        @Length(min = 4, max = 40)
        private String username;

        @NotBlank
        @Length(min = 8, max = 30)
        private String password;

        @Length(max = 40)
        @NotBlank
        private String email;

    }

    @Getter
    @Setter
    @ToString
    @ApiModel(value = "MemberDto-LoginForm")
    public static class LoginForm {

        @NotBlank
        @Length(min = 4, max = 40)
        private String username;

        @NotBlank
        @Length(min = 8, max = 30)
        private String password;

    }

    @Getter
    @Setter
    @ToString
    @ApiModel(value = "MemberDto-PasswordChangeForm")
    public static class passwordChangeForm {

        @NotBlank
        @Length(min = 8, max = 20)
        private String password;

    }

    @Getter
    @Setter
    @ApiModel(value = "MemberDto-User")
    public static class User{
        private Long id;
        private String role;

        public User(Long id, String role) {
            this.id = id;
            this.role = role;
        }
    }

}
