package guckflix.backend.service;

import guckflix.backend.dto.request.PagingRequest;
import guckflix.backend.dto.ReviewDto;
import guckflix.backend.dto.response.paging.Paging;
import guckflix.backend.entity.Movie;
import guckflix.backend.entity.Review;
import guckflix.backend.repository.MovieRepository;
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
    private final MovieRepository movieRepository;

    public Paging<ReviewDto> findAllById(Long movieId, PagingRequest pagingRequest) {
        Paging<Review> reviews = reviewRepository.findByMovieId(movieId, pagingRequest);
        List<ReviewDto> dtos = new ArrayList<>();
        for (Review findReview : reviews.getList()) {
            dtos.add(new ReviewDto(findReview));
        }
        return reviews.convert(dtos);
    }

    public ReviewDto findById(Long reviewId){
        Review entity = reviewRepository.findById(reviewId);
        return new ReviewDto(entity);
    }

    @Transactional
    public Long save(ReviewDto dto){
        Long reviewId = reviewRepository.save(dtoToEntity(dto));
        Movie movie = movieRepository.findById(dto.getMovieId());
        movie.updateVoteAdd(dto.getVoteRating());
        return reviewId;
    }

    @Transactional
    public void delete(ReviewDto dto){
        reviewRepository.remove(dtoToEntity(dto));
        Movie movie = movieRepository.findById(dto.getMovieId());
        movie.updateVoteDelete(dto.getVoteRating());
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