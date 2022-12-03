package guckflix.backend.repository;

import guckflix.backend.entity.Movie;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class MovieRepositoryTest {

    @Autowired MovieRepository movieRepository;

    @Autowired
    EntityManager em;

    @Test
    @DisplayName("리포지토리 스프링 AOP 예외전환 Test")
    public void test(){
        assertThatThrownBy(()-> movieRepository.findById(5050000L))
                .isInstanceOf(EmptyResultDataAccessException.class);
    }

    @Test
    @DisplayName("리포지토리 findById 테스트")
    public void findById(){
        movieRepository.save(
                Movie.builder()
                        .id(99999999L)
                        .title("테스트")
                        .build()
        );
        em.flush();
        em.clear();
        assertThat(movieRepository.findById(99999999L).getTitle()).isEqualTo("테스트");
    }



}