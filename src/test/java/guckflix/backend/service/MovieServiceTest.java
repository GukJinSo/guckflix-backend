package guckflix.backend.service;

import guckflix.backend.config.GenreCached;
import guckflix.backend.dto.MovieDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import javax.transaction.Transactional;

import java.time.LocalDate;

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
        MovieDto.Post movieDto = new MovieDto.Post();
        movieDto.setReleaseDate(LocalDate.parse("2022-11-26"));
        movieDto.setGenres(GenreCached.genreToListEntry("13,15"));
        movieDto.setTitle("테스트");
        movieService.save(movieDto);
    }
}