package guckflix.backend.controller;

import guckflix.backend.dto.request.ReviewRequest;
import guckflix.backend.dto.response.CreditDto;
import guckflix.backend.dto.response.MovieDto;
import guckflix.backend.dto.response.ReviewDto;
import guckflix.backend.dto.response.VideoDto;
import guckflix.backend.dto.response.wrapper.CreditResponse;
import guckflix.backend.dto.response.wrapper.MovieResponse;
import guckflix.backend.dto.response.wrapper.ReviewResponse;
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
    public ResponseEntity<MovieResponse> popular(@RequestParam(required = false, defaultValue = "1") int page,
                                                 @RequestParam(defaultValue = "20") int limit) {
        // 뷰에서 볼 페이지 요청 번호. 0페이지여도 1페이지, 1페이지면 1페이지라고 나와야 함
        int paging = page == 0 ? 1 : page;
        // 몇 번째 컬럼부터 볼 건지 정할 offset
        int offset = page - 1 >= 0 ? (page - 1 ) * limit : 0;
        List<MovieDto> queryResult = movieService.findPopular(offset, limit);
        return ResponseEntity.ok(new MovieResponse(paging, queryResult));
    }

    /**
     * 투표 가중치 기준 페이징
     * 투표 가중치 : guckflix.backend.config.QueryWeight
     */
    @GetMapping("/movies/top-rated")
    public ResponseEntity<MovieResponse> topRated(@RequestParam(required = false, defaultValue = "1") int page,
                                                  @RequestParam(defaultValue = "20") int limit) {
        int paging = page == 0 ? 1 : page;
        int offset = page - 1 >= 0 ? (page - 1 ) * limit : 0;
        List<MovieDto> queryResult = movieService.findTopRated(offset, limit);
        return ResponseEntity.ok(new MovieResponse(paging, queryResult));
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
    public ResponseEntity<MovieResponse> similar(@PathVariable Long movieId,
                                                 @RequestParam(required = false, defaultValue = "1") int page,
                                                 @RequestParam(defaultValue = "20") int limit) {
        int paging = page == 0 ? 1 : page;
        int offset = page - 1 >= 0 ? (page - 1 ) * limit : 0;
        List<MovieDto> queryResult = movieService.findSimilar(movieId, offset, limit);
        return ResponseEntity.ok(new MovieResponse(paging, queryResult));
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
    public ResponseEntity<ReviewResponse> reviews(@PathVariable Long movieId,
                                                  @RequestParam(required = false, defaultValue = "1") int page,
                                                  @RequestParam(defaultValue = "20") int limit) {
        int paging = page == 0 ? 1 : page;
        int offset = page - 1 >= 0 ? (page - 1 ) * limit : 0;
        List<ReviewDto> result = reviewService.findAllById(movieId, offset, limit);
        return ResponseEntity.ok().body(new ReviewResponse(paging,result));
    }

    /**
     * 리뷰 작성
     */
    @PostMapping(value = "/movies/{movieId}/reviews")
    public ResponseEntity<ReviewDto> reviewsPost(@PathVariable Long movieId,
    @RequestBody ReviewRequest form) {
        ReviewDto dto = new ReviewDto();
        dto.setMovieId(movieId);
        dto.setContent(form.getContent());
        dto.setVoteRating(form.getVoteRating());
        Long findId = reviewService.save(dto);
        return ResponseEntity.ok().body(reviewService.findById(findId));
    }

//
//    /**
//     * 영화 검색
//     */
//    @GetMapping("/movies/search")
//    public String () {
//        return null;
//    }
//
        //    /**



}
