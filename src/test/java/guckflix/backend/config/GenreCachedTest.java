package guckflix.backend.config;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class GenreCachedTest {

    @Test
    public void immutableTest() throws Exception {
        Map<Long, String> genres = GenreCached.getGenres();
        Assertions.assertThatThrownBy(()->genres.put(19L, "들어가면 안 됨"))
                .isInstanceOf(UnsupportedOperationException.class);
    }

}