package guckflix.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import guckflix.backend.entity.Credit;
import lombok.Getter;
import lombok.Setter;

public class CreditDto {

    @Getter
    @Setter
    public static class Response{

        @JsonProperty("actor_id")
        private Long id;

        private String name;

        @JsonProperty("character")
        private String casting;

        @JsonProperty("profile_path")
        private String profilePath;

        private int order;

        public Response (Credit entity){
            this.id = entity.getActor().getId();
            this.name = entity.getActor().getName();
            this.casting = entity.getCasting();
            this.order = entity.getOrder();
            this.profilePath = entity.getActor().getProfilePath();
        }
    }

}
