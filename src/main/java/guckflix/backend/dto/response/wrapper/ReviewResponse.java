package guckflix.backend.dto.response.wrapper;

import guckflix.backend.dto.response.ReviewDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ReviewResponse {
    private int page;

    private List<ReviewDto> result;

    public ReviewResponse(int page, List<ReviewDto> result) {
        this.page = page;
        this.result = result;
    }
}
