package guckflix.backend.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreditDto {

    @JsonProperty("actor_id")
    private Long id;

    private String name;

    @JsonProperty("character")
    private String casting;

    @JsonProperty("profile_path")
    private String profilePath;

    private int order;

}
