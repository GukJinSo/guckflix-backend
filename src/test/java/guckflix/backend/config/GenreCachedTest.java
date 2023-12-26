package guckflix.backend.config;

import guckflix.backend.entity.Genre;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class GenreCachedTest {

    @Test
    public void immutableTest() throws Exception {
        Map<Long, Genre> genres = GenreCached.getGenres();
        Assertions.assertThatThrownBy(()->genres.put(100L, Genre.builder().genreName("테스트용장르").id(100L).build()))
                .isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    public void immutableTestAndCannotInsert() throws Exception {
        Map<Long, Genre> genres = GenreCached.getGenres();
        Assertions.assertThatThrownBy(()->genres.put(100L, Genre.builder().genreName("테스트용장르").id(100L).build()))
                .isInstanceOf(UnsupportedOperationException.class);
    }

}