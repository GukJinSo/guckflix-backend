package guckflix.backend.controller;

import guckflix.backend.dto.ReviewDto.Post;
import guckflix.backend.dto.ReviewDto.Response;
import guckflix.backend.dto.paging.PagingRequest;
import guckflix.backend.dto.CreditDto;
import guckflix.backend.dto.MovieDto;
import guckflix.backend.dto.VideoDto;
import guckflix.backend.dto.paging.Slice;
import guckflix.backend.dto.wrapper.CreditResponseWrapper;
import guckflix.backend.dto.paging.Paging;
import guckflix.backend.dto.wrapper.VideoResponseWrapper;
import guckflix.backend.file.FileConst;
import guckflix.backend.file.FileUploader;
import guckflix.backend.security.authen.PrincipalDetails;
import guckflix.backend.service.CreditService;
import guckflix.backend.service.MovieService;
import guckflix.backend.service.ReviewService;
import guckflix.backend.service.VideoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    private final FileUploader fileUploader;

    /**
     * popularity 기준 페이징
     */
    @GetMapping("/movies/popular")
    @ApiOperation(value = "인기 영화 리스트", notes = "PagingRequest로 인기 영화 조회")
    public ResponseEntity<Paging> popular(PagingRequest pagingRequest) {
        Paging<MovieDto.Response> popular = movieService.findPopular(pagingRequest);
        return ResponseEntity.ok(popular);
    }


    /**
     * 투표 가중치 기준 페이징
     * 투표 가중치 : guckflix.backend.config.QueryWeight
     */
    @GetMapping("/movies/top-rated")
    @ApiOperation(value = "명작 영화 리스트", notes = "PagingRequest로 평점이 높은 영화 조회")
    public ResponseEntity<Paging> topRated(PagingRequest pagingRequest) {
        Paging<MovieDto.Response> topRated = movieService.findTopRated(pagingRequest);
        return ResponseEntity.ok(topRated);
    }

    /**
     * 영화 상세 보기
     */
    @GetMapping("/movies/{movieId}")
    @ApiOperation(value = "영화 상세", notes = "ID로 영화 상세 조회")
    public ResponseEntity<MovieDto.Response> detail(@PathVariable Long movieId) {
        MovieDto.Response findMovie = movieService.findById(movieId);
        return ResponseEntity.ok(findMovie);
    }

    /**
     * 유사한 영화 보기
     */
    @GetMapping("/movies/{movieId}/similar")
    @ApiOperation(value = "유사 영화 리스트", notes = "ID로 영화 상세를 조회한 뒤, PagingRequest로 유사 영화 조회")
    public ResponseEntity<Paging> similar(@PathVariable Long movieId,
                                          PagingRequest pagingRequest) {
        Paging<MovieDto.Response> similar = movieService.findSimilar(movieId, pagingRequest);
        return ResponseEntity.ok(similar);
    }

    /**
     * 크레딧(배역, 배우) 보기
     */
    @GetMapping("/movies/{movieId}/credits")
    @ApiOperation(value = "영화 크레딧 리스트", notes = "ID로 영화 상세를 조회한 뒤, 영화 크레딧과 배우 조회")
    public ResponseEntity<CreditResponseWrapper> credits(@PathVariable Long movieId) {
        List<CreditDto.Response> credit = creditService.findActors(movieId);
        return ResponseEntity.ok(new CreditResponseWrapper(movieId, credit));
    }

    /**
     * 영화 비디오(트레일러 등) 리스트
     * accept-language : ko or en
     */
    @GetMapping("/movies/{movieId}/videos")
    @ApiOperation(value = "영상물 조회", notes = "ID로 영화 상세를 조회하고, Locale에 따라 언어별 영상 조회")
    public ResponseEntity<VideoResponseWrapper> videos(@PathVariable Long movieId,
                                                       Locale locale) {
        List<VideoDto.Response> result = videoService.findById(movieId, locale.getLanguage());
        return ResponseEntity.ok(new VideoResponseWrapper(movieId, result));
    }

    /**
     * 리뷰 조회
     */
    @GetMapping("/movies/{movieId}/reviews")
    @ApiOperation(value = "리뷰 조회", notes = "ID로 영화 상세를 조회하고, PagingRequest에 따라 리뷰 조회")
    public ResponseEntity<Paging> reviews(@PathVariable Long movieId, PagingRequest pagingRequest) {
        Paging<Response> reviews = reviewService.findAllById(movieId, pagingRequest);
        return ResponseEntity.ok().body(reviews);
    }

    /**
     * 리뷰 작성
     */
    @PostMapping("/movies/{movieId}/reviews")
    @ApiOperation(value = "리뷰 작성", notes = "ID로 영화를 조회하고, 회원일 때 제출된 데이터로 리뷰 작성")
    public ResponseEntity<Response> reviewsPost(@PathVariable Long movieId,
                                                @AuthenticationPrincipal PrincipalDetails user,
                                                @ModelAttribute Post dto) {
        dto.setMovieId(movieId);
        dto.setUserId(user.getMember().getId());
        Long findId = reviewService.save(dto);
        return ResponseEntity.ok().body(reviewService.findById(findId));
    }

    @DeleteMapping("/movies/{movieId}/reviews/{reviewId}")
    @ApiOperation(value = "리뷰 삭제", notes = "ID로 영화를 조회하고, 회원일 때 제출된 데이터로 리뷰 삭제")
    public ResponseEntity<String> reviewsDelete(@PathVariable Long movieId,
                                                @AuthenticationPrincipal PrincipalDetails user,
                                                @PathVariable Long reviewId) {
        reviewService.delete(reviewId, movieId, user.getMember().getId());
        return ResponseEntity.ok().body("DELETED");
    }

    /**
     * 영화 검색
     */
    @GetMapping("/movies/search")
    @ApiOperation(value = "영화 검색", notes = "키워드로 영화 조회")
    public ResponseEntity<Slice> search(@RequestParam String keyword, PagingRequest pagingRequest) {
        Slice<MovieDto.Response> movies = movieService.findByKeyword(keyword, pagingRequest);
        return ResponseEntity.ok().body(movies);
    }


    /**
     * 영화 등록
     */
    @ApiOperation(value = "영화 등록", notes = "영화 프로필 등록")
    @PostMapping(value = "/movies", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity post(@RequestPart MovieDto.Post form,
                               @RequestPart MultipartFile originFile,
                               @RequestPart MultipartFile w500File){
        String originUUID = fileUploader.upload(originFile, FileConst.DIRECTORY_ORIGINAL, ".jpg");
        String w500UUID = fileUploader.upload(w500File, FileConst.DIRECTORY_W500, ".jpg");
        form.setBackdropPath(originUUID);
        form.setPosterPath(w500UUID);
        movieService.save(form);
        return new ResponseEntity(HttpStatus.CREATED);
    };

    /**
     * 영화 수정
     */
    @PatchMapping("/movies/{movieId}")
    @ApiOperation(value = "영화 수정", notes = "영화 프로필과 크레딧을 변경")
    public ResponseEntity update(
                                @PathVariable Long movieId,
                                @RequestPart MultipartFile w500File,
                                @RequestPart MultipartFile originFile,
                                @RequestPart MovieDto.Update movieUpdateForm,
                                @RequestPart CreditDto.Update creditUpdateForm){
        MovieDto.Response dto = movieService.findById(movieId);

        fileUploader.delete(FileConst.DIRECTORY_ORIGINAL, dto.getBackdropPath());
        fileUploader.delete(FileConst.DIRECTORY_W500, dto.getPosterPath());
        String originUUID = fileUploader.upload(originFile, FileConst.DIRECTORY_ORIGINAL, ".jpg");
        String w500UUID = fileUploader.upload(w500File, FileConst.DIRECTORY_W500, ".jpg");
        movieUpdateForm.setBackdropPath(originUUID);
        movieUpdateForm.setPosterPath(w500UUID);

        if (creditUpdateForm.getFormList() != null || creditUpdateForm.getFormList().size() != 0) {
            movieService.update(creditUpdateForm, movieUpdateForm, movieId);
        }

        return new ResponseEntity(HttpStatus.OK);
    }

    /**
     * 영화 크레딧 한 줄 삭제
     */
    @DeleteMapping("/movies/{movieId}/{creditId}")
    @ApiOperation(value = "영화 크레딧 삭제", notes = "영화 수정 페이지에서 크레딧을 한 줄 삭제")
    public ResponseEntity creditDelete(@PathVariable Long movieId,
                                       @PathVariable Long creditId){
        creditService.delete(creditId);
        return new ResponseEntity(HttpStatus.OK);
    }



}