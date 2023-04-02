package guckflix.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import guckflix.backend.entity.Actor;
import guckflix.backend.entity.Credit;
import io.swagger.annotations.ApiModel;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ActorDto {

    @Getter
    @Setter
    @NoArgsConstructor
    @ApiModel(value = "ActorDto-Post")
    public static class Post {

        @NotBlank
        private String name;

        @NotBlank
        @JsonProperty("profile_path")
        private String profilePath;

        @Length(min = 10, max = 500)
        private String overview;

        @JsonProperty("birth_day")
        private LocalDate birthDay;

        private List<ActorPostCredit> credits;

        @Getter
        @Setter
        @AllArgsConstructor
        public static class ActorPostCredit {

            @NotBlank
            @JsonProperty("movie_id")
            private Long movieId;

            @NotBlank
            @JsonProperty("character")
            private String casting;

        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @ToString
    @ApiModel(value = "ActorDto-Response")
    public static class Response {

        private Long id;

        private String name;

        @JsonProperty("profile_path")
        private String profilePath;

        private String overview;

        @JsonProperty("birth_day")
        private LocalDate birthDay;

        private List<ActorResponseCredit> credits;

        public Response (Actor actorDetail) {
            this.id = actorDetail.getId();
            this.name = actorDetail.getName();
            this.profilePath = actorDetail.getProfilePath();
            this.overview = actorDetail.getOverview();
            this.birthDay = actorDetail.getBirthDay();

            List<ActorResponseCredit> creditDto = new ArrayList<>();
            for (Credit credit : actorDetail.getCredits()) {
                creditDto.add(new ActorResponseCredit(credit));
            }
            this.credits = creditDto;
        }

        @Getter
        @Setter
        @NoArgsConstructor
        @ToString
        public static class ActorResponseCredit {

            @JsonProperty("credit_id")
            private Long creditId;

            @JsonProperty("movie_id")
            private Long movieId;

            @JsonProperty("character")
            private String casting;

            @JsonProperty("poster_path")
            private String posterPath;

            private int order;

            public ActorResponseCredit(Credit entity) {
                this.movieId = entity.getMovie().getId();
                this.casting = entity.getCasting();
                this.posterPath = entity.getMovie().getPosterPath();
                this.order = entity.getOrder();
                this.creditId = entity.getId();
            }
        }

    }
}
