package guckflix.backend.service;

import guckflix.backend.dto.response.ReviewDto;
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

    public List<ReviewDto> findAllById(Long movieId, int offset, int limit) {
        List<Review> findReviews = reviewRepository.findByMovieId(movieId, offset, limit);
        List<ReviewDto> dtos = new ArrayList<>();
        for (Review findReview : findReviews) {
            dtos.add(reviewEntityToDto(findReview));
        }
        return dtos;
    }

    public ReviewDto findById(Long reviewId){
        Review entity = reviewRepository.findById(reviewId);
        return reviewEntityToDto(entity);
    }

    @Transactional
    public Long save(ReviewDto dto){
        return reviewRepository.save(reviewDtoToEntity(dto));
    }

    private ReviewDto reviewEntityToDto(Review entity){
        ReviewDto reviewDto = new ReviewDto();
        reviewDto.setReviewId(entity.getId());
        reviewDto.setContent(entity.getContent());
        reviewDto.setCreatedAt(entity.getCreatedAt());
        reviewDto.setLastModifiedAt(entity.getLastModifiedAt());
        reviewDto.setVoteRating(entity.getVoteRating());
        return reviewDto;
    }

    private Review reviewDtoToEntity(ReviewDto dto){
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
