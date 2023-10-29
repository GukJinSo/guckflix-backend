package guckflix.backend.annotation;

import guckflix.backend.dto.MemberDto;
import guckflix.backend.dto.MovieDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DefaultMessageCodesResolver;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.Iterator;
import java.util.Set;


@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@SpringBootTest
class DateRangeValidatorTest {

    @Autowired private Validator validator;

    @Test
    void validSuccess() {

        MovieDto.Post post = new MovieDto.Post();
        post.setReleaseDate(LocalDate.of(2555,1,1));
        Set<ConstraintViolation<MovieDto.Post>> validate = validator.validate(post);

        String errorCode = null;
        for (ConstraintViolation<MovieDto.Post> violation : validate) {
            if(violation.getMessageTemplate().equals("{DataRange}"))
            errorCode = violation.getMessage();
        }

        Assertions.assertThat(errorCode).isEqualTo("{DataRange}");



    }

}