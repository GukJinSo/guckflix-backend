package guckflix.backend.service;

import guckflix.backend.config.GenreCached;
import guckflix.backend.dto.CreditDto;
import guckflix.backend.dto.GenreDto;
import guckflix.backend.dto.MovieDto;
import guckflix.backend.entity.Actor;
import guckflix.backend.entity.Credit;
import guckflix.backend.entity.Movie;
import guckflix.backend.repository.ActorRepository;
import guckflix.backend.repository.CreditRepository;
import guckflix.backend.repository.MovieRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import java.time.LocalDate;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class MovieServiceTest {


    @Autowired
    EntityManager em;

    @Autowired
    MovieService movieService;

    @Autowired
    MovieRepository movieRepository;

    @Autowired
    ActorRepository actorRepository;

    @Autowired CreditRepository creditRepository;


    @Test
    @Transactional
    @Rollback(value = false)
    public void updateTest() throws Exception {

        Actor actor1 = Actor.builder().name("김씨").overview("잘생겼다").credits(new ArrayList<>()).build();
        Actor actor2 = Actor.builder().name("박씨").overview("못생겼다").credits(new ArrayList<>()).build();
        Actor actor3 = Actor.builder().name("황씨").overview("그럭저럭 생겼다").credits(new ArrayList<>()).build();

        Movie movie = Movie.builder().title("Test Movie")
                .overview("테스트 영화")
                .genres(GenreCached.genreListToString(Arrays.asList(new GenreDto(1L, "Test Genre"))))
                .backdropPath("uuid1.jpg")
                .posterPath("uuid2.jpg")
                .credits(new ArrayList<>())
                .releaseDate(LocalDate.now())
                .build();

        Credit credit = Credit.builder()
                .movie(movie)
                .actor(actor1)
                .casting("쓰레기통")
                .order(0)
                .build();
        actor1.getCredits().add(credit);
        movie.getCredits().add(credit);

        movieRepository.save(movie);
        actorRepository.save(actor1);
        actorRepository.save(actor2);
        actorRepository.save(actor3);
        creditRepository.save(credit);

        CreditDto.Update creditUpdateForm = new CreditDto.Update();
        creditUpdateForm.setFormList(Arrays.asList(
                new CreditDto.Update.Form(actor1.getId(), movie.getId(), "더덕", 0),
                new CreditDto.Update.Form(actor2.getId(), movie.getId(), "감자", 1),
                new CreditDto.Update.Form(actor3.getId(), movie.getId(), "고구마", 2)
        ));

        MovieDto.Update movieUpdateForm = new MovieDto.Update();
        movieUpdateForm.setId(movie.getId());
        movieUpdateForm.setPosterPath("포스터바뀌었나요??.jpg");
        movieUpdateForm.setBackdropPath("백그라운드바뀌었나요??.jpg");
        movieUpdateForm.setGenres(Arrays.asList(new GenreDto(2L, "Genre Changed")));
        movieUpdateForm.setReleaseDate(LocalDate.of(1982,7,13));

        movieService.update(creditUpdateForm, movieUpdateForm, movie.getId());

        em.flush();
        em.clear();

        Movie findMovie = movieRepository.findById(movieUpdateForm.getId());

        assertThat(findMovie.getCredits().size()).isEqualTo(3);
        assertThat(findMovie.getReleaseDate()).isEqualTo(LocalDate.of(1982, 7, 13));

    }

    @Test
    @Transactional
    @Rollback(value = false)
    public void delete_test() throws Exception{
        Actor actor1 = Actor.builder().name("김씨").overview("잘생겼다").credits(new ArrayList<>()).build();
        Actor actor2 = Actor.builder().name("박씨").overview("못생겼다").credits(new ArrayList<>()).build();

        actorRepository.save(actor1);
        actorRepository.save(actor2);

        MovieDto.Post movieDto = new MovieDto.Post();
        movieDto.setTitle("테스트 영화");
        movieDto.setOverview("재미있음");
        movieDto.setGenres(Arrays.asList(new GenreDto(1L, "Adventure")));

        Long savedId = movieService.save(movieDto);
        Movie movie = movieRepository.findById(savedId);

        Credit credit = Credit.builder()
                .movie(movie)
                .actor(actor1)
                .casting("김씨 역")
                .order(0)
                .build();
        Credit credit2 = Credit.builder()
                .movie(movie)
                .actor(actor2)
                .casting("박씨 역")
                .order(1)
                .build();

        movie.getCredits().add(credit);
        movie.getCredits().add(credit2);

        creditRepository.save(credit);
        creditRepository.save(credit2);

        movieService.delete(movie.getId());

        assertThatThrownBy(()-> movieService.findById(movie.getId()))
                .isInstanceOf(RuntimeException.class);

    }



}