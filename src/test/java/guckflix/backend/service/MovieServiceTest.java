package guckflix.backend.service;

import guckflix.backend.dto.MovieDto;
import guckflix.backend.entity.Movie;
import guckflix.backend.exception.RuntimeMovieNotFoundException;
import guckflix.backend.repository.MovieRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import javax.persistence.NoResultException;
import javax.transaction.Transactional;

import java.text.ParseException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class MovieServiceTest {

    @Autowired
    MovieService movieService;

    @Test
    @Transactional
    @Rollback(value = false)
    public void saveTest() throws Exception {
        MovieDto movieDto = new MovieDto();
        movieDto.setReleaseDate("2022-11-26");
        movieDto.setGenres(movieDto.genreToListEntry("13,15"));
        movieDto.setTitle("테스트");
        movieService.save(movieDto);
    }
}