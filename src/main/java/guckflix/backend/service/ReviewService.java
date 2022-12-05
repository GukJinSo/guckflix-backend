package guckflix.backend.service;

import guckflix.backend.dto.request.PagingRequest;
import guckflix.backend.dto.response.MovieDto;
import guckflix.backend.dto.response.ReviewDto;
import guckflix.backend.dto.response.wrapper.Paging;
import guckflix.backend.entity.Review;
import guckflix.backend.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;

    public Paging<ReviewDto> findAllById(Long movieId, PagingRequest pagingRequest) {
        Paging<Review> reviews = reviewRepository.findByMovieId(movieId, pagingRequest);
        List<ReviewDto> dtos = new ArrayList<>();
        for (Review findReview : reviews.getList()) {
            dtos.add(new ReviewDto(findReview));
        }
        return new Paging<ReviewDto>(reviews.getRequestPage(), dtos, reviews.getTotalCount(),reviews.getTotalPage(), reviews.getSize());
    }

    public ReviewDto findById(Long reviewId){
        Review entity = reviewRepository.findById(reviewId);
        return new ReviewDto(entity);
    }

    @Transactional
    public Long save(ReviewDto dto){
        return reviewRepository.save(dtoToEntity(dto));
    }

    private Review dtoToEntity(ReviewDto dto){
        Review entity = Review.builder()
                .id(dto.getReviewId())
                .userId(dto.getUserId())
                .content(dto.getContent())
                .voteRating(dto.getVoteRating())
                .movieId(dto.getMovieId())
                .build();
        return entity;
    }

}
