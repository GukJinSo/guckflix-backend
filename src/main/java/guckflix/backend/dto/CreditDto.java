package guckflix.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import guckflix.backend.entity.Credit;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class CreditDto {

    @Getter
    @Setter
    @ApiModel(value = "CreditDto-Response")
    public static class Response{

        @JsonProperty("actor_id")
        private Long id;

        private String name;

        @JsonProperty("movie_id")
        private Long movieId;

        @JsonProperty("character")
        private String casting;

        @JsonProperty("profile_path")
        private String profilePath;

        private int order;

        public Response (Credit entity){
            this.id = entity.getActor().getId();
            this.movieId = entity.getMovie().getId();
            this.name = entity.getActor().getName();
            this.casting = entity.getCasting();
            this.order = entity.getOrder();
            this.profilePath = entity.getActor().getProfilePath();
        }
    }

    @Getter
    @Setter
    @ApiModel(value = "CreditDto-Post")
    public static class Post {

        private List<Form> formList;

        @Getter
        @Setter
        @ApiModel(value = "CreditDto-Post-Form")
        public static class Form{

            @JsonProperty("actor_id")
            private Long id;

            private String name;

            @JsonProperty("movie_id")
            private Long movieId;

            @JsonProperty("character")
            private String casting;

            private int order;

        }

    }

    @Getter
    @Setter
    @ApiModel(value = "CreditDto-Update")
    public static class Update {

        private List<Form> formList;

        @Getter
        @Setter
        @ApiModel(value = "CreditDto-Update-Form")
        public static class Form{

            private Long id;

            @JsonProperty("actor_id")
            private Long actorId;

            @JsonProperty("movie_id")
            private Long movieId;

            @JsonProperty("character")
            private String casting;

            private int order;

            public Form(Long actorId, Long movieId, String casting, int order) {
                this.actorId = actorId;
                this.movieId = movieId;
                this.casting = casting;
                this.order = order;
            }
        }

    }

}
