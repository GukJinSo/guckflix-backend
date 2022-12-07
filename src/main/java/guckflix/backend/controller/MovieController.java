package guckflix.backend.controller;

import guckflix.backend.dto.request.PagingRequest;
import guckflix.backend.dto.CreditDto;
import guckflix.backend.dto.MovieDto;
import guckflix.backend.dto.ReviewDto;
import guckflix.backend.dto.VideoDto;
import guckflix.backend.dto.response.paging.Slice;
import guckflix.backend.dto.response.wrapper.CreditResponse;
import guckflix.backend.dto.response.paging.Paging;
import guckflix.backend.dto.response.wrapper.VideoResponse;
import guckflix.backend.security.authen.PrincipalDetails;
import guckflix.backend.service.CreditService;
import guckflix.backend.service.MovieService;
import guckflix.backend.service.ReviewService;
import guckflix.backend.service.VideoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;

@Slf4j
@Api(tags = {"영화 API"})
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
    @ApiOperation(value = "인기 영화 리스트", notes = "PagingRequest로 인기 영화 조회")
    public ResponseEntity<Paging> popular(PagingRequest pagingRequest) {
        Paging<MovieDto> popular = movieService.findPopular(pagingRequest);
        return ResponseEntity.ok(popular);
    }


    /**
     * 투표 가중치 기준 페이징
     * 투표 가중치 : guckflix.backend.config.QueryWeight
     */
    @GetMapping("/movies/top-rated")
    @ApiOperation(value = "명작 영화 리스트", notes = "PagingRequest로 평점이 높은 영화 조회")
    public ResponseEntity<Paging> topRated(PagingRequest pagingRequest) {
        Paging<MovieDto> topRated = movieService.findTopRated(pagingRequest);
        return ResponseEntity.ok(topRated);
    }

    /**
     * 영화 상세 보기
     */
    @GetMapping("/movies/{movieId}")
    @ApiOperation(value = "영화 상세", notes = "ID로 영화 상세 조회")
    public ResponseEntity<MovieDto> detail(@PathVariable Long movieId) {
        MovieDto findMovie = movieService.findById(movieId);
        return ResponseEntity.ok(findMovie);
    }

    /**
     * 유사한 영화 보기
     */
    @GetMapping("/movies/{movieId}/similar")
    @ApiOperation(value = "유사 영화 리스트", notes = "ID로 영화 상세를 조회한 뒤, PagingRequest로 유사 영화 조회")
    public ResponseEntity<Paging> similar(@PathVariable Long movieId,
                                          PagingRequest pagingRequest) {
        Paging<MovieDto> similar = movieService.findSimilar(movieId, pagingRequest);
        return ResponseEntity.ok(similar);
    }

    /**
     * 크레딧(배역, 배우) 보기
     */
    @GetMapping("/movies/{movieId}/credits")
    @ApiOperation(value = "영화 크레딧 리스트", notes = "ID로 영화 상세를 조회한 뒤, 영화 크레딧과 배우 조회")
    public ResponseEntity<CreditResponse> credits(@PathVariable Long movieId) {
        List<CreditDto> credit = creditService.findActors(movieId);
        return ResponseEntity.ok(new CreditResponse(movieId, credit));
    }

    /**
     * 영화 비디오(트레일러 등) 리스트
     * accept-language : ko or en
     */
    @GetMapping("/movies/{movieId}/videos")
    @ApiOperation(value = "영상물 조회", notes = "ID로 영화 상세를 조회하고, Locale에 따라 언어별 영상 조회")
    public ResponseEntity<VideoResponse> videos(@PathVariable Long movieId,
                                                Locale locale) {
        List<VideoDto> result = videoService.findById(movieId, locale.getLanguage());
        return ResponseEntity.ok(new VideoResponse(movieId, result));
    }

    /**
     * 리뷰 조회
     */
    @GetMapping("/movies/{movieId}/reviews")
    @ApiOperation(value = "리뷰 조회", notes = "ID로 영화 상세를 조회하고, PagingRequest에 따라 리뷰 조회")
    public ResponseEntity<Paging> reviews(@PathVariable Long movieId, PagingRequest pagingRequest) {
        Paging<ReviewDto> reviews = reviewService.findAllById(movieId, pagingRequest);
        return ResponseEntity.ok().body(reviews);
    }

    /**
     * 리뷰 작성
     */
    @PostMapping("/movies/{movieId}/reviews")
    @ApiOperation(value = "리뷰 작성", notes = "ID로 영화를 조회하고, 회원일 때 ReviewDto에 제출된 데이터로 리뷰 작성")
    public ResponseEntity<ReviewDto> reviewsPost(@PathVariable Long movieId,
                                                 @AuthenticationPrincipal PrincipalDetails user,
                                                 @ModelAttribute ReviewDto dto) {
        dto.setMovieId(movieId);
        dto.setUserId(user.getMember().getId());
        Long findId = reviewService.save(dto);
        return ResponseEntity.ok().body(reviewService.findById(findId));
    }

    @DeleteMapping("/movies/{movieId}/reviews/{reviewId}")
    @ApiOperation(value = "리뷰 작성", notes = "ID로 영화를 조회하고, 회원일 때 ReviewDto에 제출된 데이터로 리뷰 삭제")
    public ResponseEntity<String> reviewsDelete(@PathVariable Long movieId,
                                                @AuthenticationPrincipal PrincipalDetails user,
                                                @PathVariable Long reviewId) {
        ReviewDto dto = new ReviewDto();
        dto.setMovieId(movieId);
        dto.setUserId(user.getMember().getId());
        dto.setReviewId(reviewId);
        reviewService.delete(dto);
        return ResponseEntity.ok().body("DELETED");
    }

    /**
     * 영화 검색
     */
    @GetMapping("/movies/search")
    @ApiOperation(value = "리뷰 작성", notes = "ID로 영화를 조회하고, 회원일 때 ReviewDto에 제출된 데이터로 리뷰 삭제")
    public ResponseEntity<Slice> search(@RequestParam String keyword, PagingRequest pagingRequest) {
        Slice<MovieDto> movies = movieService.findByKeyword(keyword, pagingRequest);
        return ResponseEntity.ok().body(movies);
    }





}