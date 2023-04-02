package guckflix.backend.service;

import guckflix.backend.dto.ActorDto;
import guckflix.backend.dto.MovieDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class ActorServiceTest {

    @Autowired ActorService actorService;
    @Autowired MovieService movieService;

    @Autowired
    EntityManager entityManager;

    Long savedMovieId1;
    Long savedMovieId2;
    Long savedMovieId3;
    Long savedMovieId4;


    @BeforeEach
    public void beforeEach() {

        MovieDto.Post movie = new MovieDto.Post();
        movie.setTitle("쇼생크탈출");
        movie.setOverview("감옥에 억울하게 ...");
        movie.setReleaseDate(LocalDate.of(1945,11,26));
        MovieDto.Post movie2 = new MovieDto.Post();
        movie2.setTitle("어벤저스");
        movie2.setOverview("히어로들이 ...");
        movie2.setReleaseDate(LocalDate.of(2021,11,26));
        MovieDto.Post movie3 = new MovieDto.Post();
        movie3.setTitle("대부");
        movie3.setOverview("신참 마피아 ...");
        movie3.setReleaseDate(LocalDate.of(1966,11,26));
        MovieDto.Post movie4 = new MovieDto.Post();
        movie4.setTitle("이퀼리브리엄");
        movie4.setOverview("불법 색출 ...");
        movie4.setReleaseDate(LocalDate.of(2006,5,26));

        savedMovieId1 = movieService.save(movie);
        savedMovieId2 = movieService.save(movie2);
        savedMovieId3 = movieService.save(movie3);
        savedMovieId4 = movieService.save(movie4);
    }

    @Test
    @Rollback(value = false)
    public void saveTest() throws Exception{

        ActorDto.Post form = new ActorDto.Post();
        form.setBirthDay(LocalDate.of(1994,11,26));
        form.setName("gukjin");
        form.setOverview("1994년 대구에서 출생한 ....");
        form.setCredits(Arrays.asList(
                new ActorDto.Post.ActorPostCredit(savedMovieId1, "수감자1"),
                new ActorDto.Post.ActorPostCredit(savedMovieId2, "국장"),
                new ActorDto.Post.ActorPostCredit(savedMovieId3, "주인공")
        ));

        ActorDto.Post form2 = new ActorDto.Post();
        form2.setBirthDay(LocalDate.of(1954,7,30));
        form2.setName("hwan");
        form2.setOverview("1954년 서울에서 출생한 ....");
        form2.setCredits(Arrays.asList(
                new ActorDto.Post.ActorPostCredit(savedMovieId1, "수감자1")
        ));

        Long savedId = actorService.save(form);
        entityManager.flush();
        entityManager.clear();
        Long savedId2 = actorService.save(form2);
        entityManager.flush();
        entityManager.clear();

        ActorDto.Response gukjin = actorService.findDetail(savedId);
        ActorDto.Response hwan = actorService.findDetail(savedId2);

        assertThat(gukjin.getName()).isEqualTo("gukjin");
        assertThat(hwan.getCredits().get(0).getOrder()).isEqualTo(1);
        System.out.println("hwan = " + hwan);
        System.out.println("hwan.getCredits() = " + hwan.getCredits());

    }


}