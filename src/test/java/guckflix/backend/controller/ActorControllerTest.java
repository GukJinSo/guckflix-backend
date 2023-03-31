package guckflix.backend.controller;

import guckflix.backend.dto.ActorDto;
import guckflix.backend.dto.MovieDto;
import guckflix.backend.service.ActorService;
import guckflix.backend.service.MovieService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@SpringBootTest
class ActorControllerTest {

    @Autowired MockMvc mockMvc;

    @Autowired ActorService actorService;
    @Autowired MovieService movieService;

    @BeforeEach
    private void before(){

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

        movieService.save(movie);
        movieService.save(movie2);
        movieService.save(movie3);

        ActorDto.Post form = new ActorDto.Post();
        form.setBirthDay(LocalDate.of(1994,11,26));
        form.setName("gukjin");
        form.setOverview("1994년 대구에서 출생한 ....");
        form.setCredits(Arrays.asList(
                new ActorDto.Post.ActorPostCredit(1L, "수감자1"),
                new ActorDto.Post.ActorPostCredit(2L, "국장"),
                new ActorDto.Post.ActorPostCredit(3L, "주인공")
        ));
        Long savedId = actorService.save(form);

    }

    @Test
    @WithMockUser
    public void actorController_get_test() throws Exception{

        mockMvc.perform(
                MockMvcRequestBuilders.get("/actors/4")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(4))
                .andExpect(jsonPath("$.name").value("gukjin"));

    }
}