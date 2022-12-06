package guckflix.backend.service;

import guckflix.backend.dto.request.PagingRequest;
import guckflix.backend.dto.MovieDto;
import guckflix.backend.dto.response.paging.Paging;
import guckflix.backend.dto.response.paging.Slice;
import guckflix.backend.entity.Movie;
import guckflix.backend.exception.RuntimeMovieNotFoundException;
import guckflix.backend.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

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
            throw new RuntimeMovieNotFoundException("No movie of given id", e);
        }
    }

    public Paging<MovieDto> findSimilar(Long id, PagingRequest pagingRequest) {
        Movie findMovie = movieRepository.findById(id);
        String movieGenres = findMovie.getGenres();
        List<String> genreList = new ArrayList<>(Arrays.asList(movieGenres.split(",")));
        Paging<Movie> similar = movieRepository.findSimilarByGenres(findMovie.getId(), genreList, pagingRequest);
        List<MovieDto> dtos = similar.getList().stream()
                .map((entity) -> new MovieDto(entity)).collect(Collectors.toList());
        return similar.convert(dtos);
    }

    public Paging<MovieDto> findPopular(PagingRequest pagingRequest) {
        Paging<Movie> popular = movieRepository.findPopular(pagingRequest);
        List<MovieDto> dtos = popular.getList().stream()
                .map((entity) -> new MovieDto(entity)).collect(Collectors.toList());
        return popular.convert(dtos);
    }

    public Paging<MovieDto> findTopRated(PagingRequest pagingRequest) {
        Paging<Movie> topRated = movieRepository.findTopRated(pagingRequest);
        List<MovieDto> dtos = topRated.getList().stream()
                .map((entity) -> new MovieDto(entity)).collect(Collectors.toList());
        return topRated.convert(dtos);
    }

    public Slice<MovieDto> findByKeyword(String keyword, PagingRequest pagingRequest) {
        Slice<Movie> search = movieRepository.findByKeyword(keyword, pagingRequest);
        List<MovieDto> dtos = search.getList().stream()
                .map((entity) -> new MovieDto(entity)).collect(Collectors.toList());
        return search.convert(dtos);
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
