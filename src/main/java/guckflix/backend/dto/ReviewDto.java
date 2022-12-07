package guckflix.backend.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import guckflix.backend.entity.Review;
import guckflix.backend.entity.enums.ISO3166;
import guckflix.backend.entity.enums.ISO639;
import guckflix.backend.entity.enums.VideoProvider;
import guckflix.backend.entity.enums.VideoType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class ReviewDto {

    @JsonProperty("review_id")
    private Long reviewId;

    @JsonProperty("user_id")
    private Long userId;

    private String content;

    @JsonProperty("vote_rating")
    private float voteRating;

    @JsonProperty("last_modified_at")
    private LocalDateTime lastModifiedAt;

    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @JsonIgnore
    private Long movieId;

    public ReviewDto (Review entity){
        this.setReviewId(entity.getId());
        this.setContent(entity.getContent());
        this.setCreatedAt(entity.getCreatedAt());
        this.setLastModifiedAt(entity.getLastModifiedAt());
        this.setVoteRating(entity.getVoteRating());
    }

}