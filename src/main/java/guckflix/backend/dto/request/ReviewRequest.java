package guckflix.backend.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewRequest {

    private String content;

    @JsonProperty("vote_rating")
    private float voteRating;
}
