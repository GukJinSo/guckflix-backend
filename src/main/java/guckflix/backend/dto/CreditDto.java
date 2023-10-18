package guckflix.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import guckflix.backend.entity.Credit;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

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

        @JsonProperty("actor_id")
        private Long actorId;

        @JsonProperty("character")
        private String casting;

    }

    @Getter
    @Setter
    @ApiModel(value = "CreditDto-Delete")
    public static class Delete {

        @JsonProperty("credit_id")
        private Long id;

    }

    @Getter
    @Setter
    @ApiModel(value = "CreditDto-Patch")
    public static class Patch {

        @JsonProperty("character")
        private String casting;

    }

}
