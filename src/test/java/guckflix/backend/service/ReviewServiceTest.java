package guckflix.backend.service;

import guckflix.backend.config.GenreCached;
import guckflix.backend.dto.MovieDto;
import guckflix.backend.dto.ReviewDto;
import guckflix.backend.entity.Genre;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
class ReviewServiceTest {

    @Autowired ReviewService reviewService;
    @Autowired MovieService movieService;

    @Test
    @Transactional
    public void review_add() throws Exception{
        MovieDto movieDto = new MovieDto();
        movieDto.setTitle("test");
        movieDto.setVoteAverage(4f);
        movieDto.setVoteCount(5);
        movieDto.setReleaseDate("2022-11-23");
        Long savedMovieId = movieService.save(movieDto);

        ReviewDto reviewDto = new ReviewDto();
        reviewDto.setMovieId(savedMovieId);
        reviewDto.setUserId(115L);
        reviewDto.setVoteRating(1);
        Long savedReviewId = reviewService.save(reviewDto);

        MovieDto findMovie = movieService.findById(savedMovieId);
        ReviewDto findReview = reviewService.findById(savedReviewId);

        assertThat(findMovie.getVoteAverage()).isEqualTo(3.5f);
        assertThat(findMovie.getVoteCount()).isEqualTo(6);

        assertThat(findReview.getVoteRating()).isEqualTo(1);
    }

    @Test
    @Transactional
    public void review_minus() throws Exception{

        MovieDto movieDto = new MovieDto();
        movieDto.setTitle("test");

        movieDto.setVoteAverage(0);
        movieDto.setVoteCount(0);
        movieDto.setReleaseDate("2022-11-23");
        Long savedMovieId = movieService.save(movieDto);

        ReviewDto reviewDto = new ReviewDto();
        reviewDto.setMovieId(savedMovieId);
        reviewDto.setVoteRating(5);
        reviewService.save(reviewDto);

        ReviewDto reviewDto2 = new ReviewDto();
        reviewDto2.setMovieId(savedMovieId);
        reviewDto2.setVoteRating(4);
        reviewService.save(reviewDto2);

        MovieDto findMovie1 = movieService.findById(savedMovieId);

        assertThat(findMovie1.getVoteAverage()).isEqualTo(4.5f);
        assertThat(findMovie1.getVoteCount()).isEqualTo(2);

        reviewService.delete(reviewDto2);

        MovieDto findMovie2 = movieService.findById(savedMovieId); // DTO는 엔티티 조회 시마다 new로 생성되므로 새로 조회해서 비교

        assertThat(findMovie2.getVoteAverage()).isEqualTo(5f);
        assertThat(findMovie2.getVoteCount()).isEqualTo(1);

    }
}