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
        movieDto.setId(1000000L);

        movieDto.setVoteAverage(4f);
        movieDto.setVoteCount(5);
        movieDto.setReleaseDate("2022-11-23");
        movieService.save(movieDto);

        MovieDto movie = movieService.findById(movieDto.getId());
        System.out.println("movie = " + movie.getId());

        ReviewDto reviewDto = new ReviewDto();
        reviewDto.setReviewId(1000L);
        reviewDto.setMovieId(1000000L);
        reviewDto.setUserId(115L);
        reviewDto.setVoteRating(1);
        reviewService.save(reviewDto);

        MovieDto findMovie = movieService.findById(1000000L);
        ReviewDto findReview = reviewService.findById(1000L);

        assertThat(findMovie.getVoteAverage()).isEqualTo(3.5f);
        assertThat(findMovie.getVoteCount()).isEqualTo(6);

        assertThat(findReview.getVoteRating()).isEqualTo(1);
    }

    @Test
    @Transactional
    public void review_minus() throws Exception{

        MovieDto movieDto = new MovieDto();
        movieDto.setTitle("test");
        movieDto.setId(1000000L);

        movieDto.setVoteAverage(0);
        movieDto.setVoteCount(0);
        movieDto.setReleaseDate("2022-11-23");
        movieService.save(movieDto);

        MovieDto movie = movieService.findById(movieDto.getId());
        System.out.println("movie = " + movie.getId());

        ReviewDto reviewDto = new ReviewDto();
        reviewDto.setReviewId(998L);
        reviewDto.setMovieId(1000000L);
        reviewDto.setVoteRating(5);
        reviewService.save(reviewDto);

        ReviewDto reviewDto2 = new ReviewDto();
        reviewDto2.setReviewId(999L);
        reviewDto2.setMovieId(1000000L);
        reviewDto2.setVoteRating(4);
        reviewService.save(reviewDto2);

        MovieDto findMovie = movieService.findById(1000000L);

        assertThat(findMovie.getVoteAverage()).isEqualTo(4.5f);
        assertThat(findMovie.getVoteCount()).isEqualTo(2);

        reviewService.delete(reviewDto2);

        MovieDto findMovie2 = movieService.findById(1000000L);

        assertThat(findMovie2.getVoteAverage()).isEqualTo(5f);
        assertThat(findMovie2.getVoteCount()).isEqualTo(1);

    }
}