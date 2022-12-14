package guckflix.backend.service;

import guckflix.backend.dto.MovieDto.Response;
import guckflix.backend.dto.paging.PagingRequest;
import guckflix.backend.dto.paging.Paging;
import guckflix.backend.dto.paging.Slice;
import guckflix.backend.entity.Movie;
import guckflix.backend.exception.MovieNotFoundException;
import guckflix.backend.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

import static guckflix.backend.dto.MovieDto.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class MovieService {

    private final MovieRepository movieRepository;

    public Response findById(Long id){
        try {
            Movie findMovie = movieRepository.findById(id);
            return new Response(findMovie);
        } catch (NullPointerException e) {
            throw new MovieNotFoundException("No movie of given id", e);
        }
    }

    public Paging<Response> findSimilar(Long id, PagingRequest pagingRequest) {
        Movie findMovie = movieRepository.findById(id);
        String movieGenres = findMovie.getGenres();
        List<String> genreList = new ArrayList<>(Arrays.asList(movieGenres.split(",")));
        Paging<Movie> similar = movieRepository.findSimilarByGenres(findMovie.getId(), genreList, pagingRequest);
        List<Response> dtos = similar.getList().stream()
                .map((entity) -> new Response(entity)).collect(Collectors.toList());
        return similar.convert(dtos);
    }

    public Paging<Response> findPopular(PagingRequest pagingRequest) {
        Paging<Movie> popular = movieRepository.findPopular(pagingRequest);
        List<Response> dtos = popular.getList().stream()
                .map((entity) -> new Response(entity)).collect(Collectors.toList());
        return popular.convert(dtos);
    }

    public Paging<Response> findTopRated(PagingRequest pagingRequest) {
        Paging<Movie> topRated = movieRepository.findTopRated(pagingRequest);
        List<Response> dtos = topRated.getList().stream()
                .map((entity) -> new Response(entity)).collect(Collectors.toList());
        return topRated.convert(dtos);
    }

    public Slice<Response> findByKeyword(String keyword, PagingRequest pagingRequest) {
        Slice<Movie> search = movieRepository.findByKeyword(keyword, pagingRequest);
        List<Response> dtos = search.getList().stream()
                .map((entity) -> new Response(entity)).collect(Collectors.toList());
        return search.convert(dtos);
    }

    public Long save(Post movieDto) {
        Movie movie = dtoToEntity(movieDto);
        return movieRepository.save(movie);
    }

    private Movie dtoToEntity(Post dto) {
        return Movie.builder()
                .title(dto.getTitle())
                .backdropPath(dto.getBackdropPath())
                .posterPath(dto.getPosterPath())
                .overview(dto.getOverview())
                .genres(genreToString(dto.getGenres()))
                .releaseDate(dto.getReleaseDate())
                .build();
    }

}
