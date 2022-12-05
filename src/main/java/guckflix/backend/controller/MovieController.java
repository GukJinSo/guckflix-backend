package guckflix.backend.controller;

import guckflix.backend.dto.request.PagingRequest;
import guckflix.backend.dto.request.ReviewRequest;
import guckflix.backend.dto.response.CreditDto;
import guckflix.backend.dto.response.MovieDto;
import guckflix.backend.dto.response.ReviewDto;
import guckflix.backend.dto.response.VideoDto;
import guckflix.backend.dto.response.paging.Slice;
import guckflix.backend.dto.response.wrapper.CreditResponse;
import guckflix.backend.dto.response.paging.Paging;
import guckflix.backend.dto.response.wrapper.VideoResponse;
import guckflix.backend.service.CreditService;
import guckflix.backend.service.MovieService;
import guckflix.backend.service.ReviewService;
import guckflix.backend.service.VideoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MovieController {

    private final MovieService movieService;
    private final CreditService creditService;
    private final VideoService videoService;
    private final ReviewService reviewService;

    /**
     * popularity 기준 페이징
     */
    @GetMapping("/movies/popular")
    public ResponseEntity<Paging> popular(PagingRequest pagingRequest) {
        Paging<MovieDto> popular = movieService.findPopular(pagingRequest);
        return ResponseEntity.ok(popular);
    }


    /**
     * 투표 가중치 기준 페이징
     * 투표 가중치 : guckflix.backend.config.QueryWeight
     */
    @GetMapping("/movies/top-rated")
    public ResponseEntity<Paging> topRated(PagingRequest pagingRequest) {
        Paging<MovieDto> topRated = movieService.findTopRated(pagingRequest);
        return ResponseEntity.ok(topRated);
    }

    /**
     * 영화 상세 보기
     */
    @GetMapping("/movies/{movieId}")
    public ResponseEntity<MovieDto> detail(@PathVariable Long movieId) {
        MovieDto findMovie = movieService.findById(movieId);
        return ResponseEntity.ok(findMovie);
    }

    /**
     * 유사한 영화 보기
     */
    @GetMapping("/movies/{movieId}/similar")
    public ResponseEntity<Paging> similar(@PathVariable Long movieId,
                                                 PagingRequest pagingRequest) {
        Paging<MovieDto> similar = movieService.findSimilar(movieId, pagingRequest);
        return ResponseEntity.ok(similar);
    }

    /**
     * 크레딧(배역, 배우) 보기
     */
    @GetMapping("/movies/{movieId}/credits")
    public ResponseEntity<CreditResponse> credits(@PathVariable Long movieId) {
        List<CreditDto> credit = creditService.findActors(movieId);
        return ResponseEntity.ok(new CreditResponse(movieId, credit));
    }

    /**
     * 영화 비디오(트레일러 등) 리스트
     */
    @GetMapping("/movies/{movieId}/videos")
    public ResponseEntity<VideoResponse> videos(@PathVariable Long movieId,
                                                Locale locale) {
        List<VideoDto> result = videoService.findById(movieId, locale.getLanguage());
        return ResponseEntity.ok(new VideoResponse(movieId, result));
    }

    /**
     * 리뷰 조회
     */
    @GetMapping("/movies/{movieId}/reviews")
    public ResponseEntity<Paging> reviews(@PathVariable Long movieId, PagingRequest pagingRequest) {
        Paging<ReviewDto> reviews = reviewService.findAllById(movieId, pagingRequest);
        return ResponseEntity.ok().body(reviews);
    }

    /**
     * 리뷰 작성
     */
    @PostMapping("/movies/{movieId}/reviews")
    public ResponseEntity<ReviewDto> reviewsPost(@PathVariable Long movieId,
    @RequestBody ReviewRequest form) {
        ReviewDto dto = new ReviewDto();
        dto.setMovieId(movieId);
        dto.setContent(form.getContent());
        dto.setVoteRating(form.getVoteRating());
        Long findId = reviewService.save(dto);
        return ResponseEntity.ok().body(reviewService.findById(findId));
    }


    /**
     * 영화 검색
     */
    @GetMapping("/movies/search")
    public ResponseEntity<Slice> search(@RequestParam String keyword, PagingRequest pagingRequest) {
        Slice<MovieDto> movies = movieService.findByKeyword(keyword, pagingRequest);
        return ResponseEntity.ok().body(movies);
    }





}
