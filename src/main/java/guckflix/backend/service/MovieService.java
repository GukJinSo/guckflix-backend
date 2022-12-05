package guckflix.backend.service;

import guckflix.backend.config.GenreCached;
import guckflix.backend.dto.request.PagingRequest;
import guckflix.backend.dto.response.MovieDto;
import guckflix.backend.dto.response.wrapper.Paging;
import guckflix.backend.entity.Movie;
import guckflix.backend.exception.RuntimeMovieNotFoundException;
import guckflix.backend.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class MovieService {

    private final MovieRepository movieRepository;


    public MovieDto findById(Long id){
        try {
            MovieDto movieDto = new MovieDto((movieRepository.findById(id)));
            return movieDto;
        } catch (EmptyResultDataAccessException e) {
            throw new RuntimeMovieNotFoundException("MOVIE NOT FOUND" , e);
        }
    }

    public Paging<MovieDto> findSimilar(Long id, PagingRequest pagingRequest) {
        Movie findMovie = movieRepository.findById(id);
        String movieGenres = findMovie.getGenres();
        List<String> genreList = new ArrayList<>(Arrays.asList(movieGenres.split(",")));
        Paging<Movie> similar = movieRepository.findSimilarByGenres(findMovie.getId(), genreList, pagingRequest);
        List<MovieDto> dtos = new ArrayList<>();
        for (Movie movie : similar.getList()) {
            MovieDto movieDto = new MovieDto(movie);
            dtos.add(movieDto);
        }
        return new Paging<MovieDto>(similar.getRequestPage(), dtos, similar.getTotalCount(),similar.getTotalPage(), similar.getSize());
    }

    public Paging<MovieDto> findPopular(PagingRequest pagingRequest) {
        Paging<Movie> popular = movieRepository.findPopular(pagingRequest);
        List<MovieDto> dtos = new ArrayList<>();
        for (Movie movie : popular.getList()) {
            dtos.add(new MovieDto(movie));
        }
        return new Paging<MovieDto>(pagingRequest.getRequestPage(), dtos, popular.getTotalCount(),popular.getTotalPage(), popular.getSize());
    }

    public Paging<MovieDto> findTopRated(PagingRequest pagingRequest) {
        Paging<Movie> topRated = movieRepository.findTopRated(pagingRequest);
        List<MovieDto> dtos = new ArrayList<>();
        for (Movie movie : topRated.getList()) {
            dtos.add(new MovieDto(movie));
        }
        return new Paging<MovieDto>(pagingRequest.getRequestPage(), dtos, topRated.getTotalCount(), topRated.getTotalPage(), topRated.getSize());
    }

    /**
    public List<MovieDto> findByKeyword(String keyword) {
        List<Movie> list = movieRepository.findByKeyword(keyword);
        List<MovieDto> dtos = new ArrayList<>();
        for (Movie movie : list) {
            dtos.add(new MovieDto(movie));
        }
        return dtos;
    }
     */
}
