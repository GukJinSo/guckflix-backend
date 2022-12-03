package guckflix.backend.service;

import guckflix.backend.exception.RuntimeMovieNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.NoResultException;
import javax.transaction.Transactional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class MovieServiceTest {

    @Autowired
    MovieService movieService;

}