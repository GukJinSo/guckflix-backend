package guckflix.backend.controller;

import guckflix.backend.dto.CreditDto;
import guckflix.backend.dto.MovieDto;
import guckflix.backend.dto.VideoDto;
import guckflix.backend.dto.response.CreditResponse;
import guckflix.backend.dto.response.MovieResponse;
import guckflix.backend.dto.response.VideoResponse;
import guckflix.backend.service.CreditService;
import guckflix.backend.service.MovieService;
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

    /**
     * popularity 기준 페이징
     */
    @GetMapping("/movie/popular")
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
    @GetMapping("/movie/top_rated")
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
    @GetMapping("/movie/{movieId}")
    public ResponseEntity<MovieDto> detail(@PathVariable Long movieId) {
        MovieDto findMovie = movieService.findById(movieId);
        return ResponseEntity.ok(findMovie);
    }

    /**
     * 유사한 영화 보기
     */
    @GetMapping("/movie/{movieId}/similar")
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
    @GetMapping("/movie/{movieId}/credits")
    public ResponseEntity<CreditResponse> credits(@PathVariable Long movieId) {
        List<CreditDto> credit = creditService.findActors(movieId);
        return ResponseEntity.ok(new CreditResponse(movieId, credit));
    }

    /**
     * 영화 비디오(트레일러 등) 리스트
     */
    @GetMapping("/movie/{movieId}/videos")
    public ResponseEntity<VideoResponse> videos(@PathVariable Long movieId,
                                                Locale locale) {
        List<VideoDto> result = videoService.findById(movieId, locale.getLanguage());
        return ResponseEntity.ok(new VideoResponse(movieId, result));
    }

    //
//    /**
//     * 평점 내기
//     */
//    @PostMapping("/movie/{movieId}/voting")
//
//    /**
//     * 영화 검색
//     */
//    @GetMapping("/movie/search")
//    public String () {
//        return null;
//    }
//
    //    /**




}
