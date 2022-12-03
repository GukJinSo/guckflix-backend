package guckflix.backend.service;

import guckflix.backend.config.GenreCached;
import guckflix.backend.dto.MovieDto;
import guckflix.backend.entity.Movie;
import guckflix.backend.exception.RuntimeMovieNotFoundException;
import guckflix.backend.repository.MovieRepository;
import guckflix.backend.repository.MovieRepositoryQuerydsl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class MovieService {

    private final MovieRepository movieRepository;
    private final MovieRepositoryQuerydsl movieRepositoryQuerydsl;

    public List<MovieDto> findPopular(int offset, int limit){
        List<Movie> popular = movieRepository.findPopular(offset, limit);
        List<MovieDto> dtos = new ArrayList<>();
        for (Movie movie : popular) {
            dtos.add(movieEntityToDto(movie));
        }
        return dtos;
    }

    public List<MovieDto> findTopRated(int offset, int limit) {
        List<Movie> topRated = movieRepository.findTopRated(offset, limit);
        List<MovieDto> dtos = new ArrayList<>();
        for (Movie movie : topRated) {
            dtos.add(movieEntityToDto(movie));
        }
        return dtos;
    }

    public MovieDto findById(Long id){
        try {
            return movieEntityToDto(movieRepository.findById(id));
        } catch (EmptyResultDataAccessException e) {
            throw new RuntimeMovieNotFoundException("MOVIE NOT FOUND" , e);
        }
    }

    public List<MovieDto> findSimilar(Long id, int offset, int limit) {
        List<Movie> similars = movieRepositoryQuerydsl.findSimilarByGenre(id, offset, limit);
        List<MovieDto> dtos = new ArrayList<>();
        for (Movie movie : similars) {
            dtos.add(movieEntityToDto(movie));
        }
        return dtos;
    }

    private MovieDto movieEntityToDto(Movie entity){
        MovieDto movieDto = new MovieDto();
        movieDto.setId(entity.getId());
        movieDto.setGenres(genreMatch(entity.getGenres()));
        movieDto.setOverview(entity.getOverview());
        movieDto.setPopularity(entity.getPopularity());
        movieDto.setTitle(entity.getTitle());
        movieDto.setPosterPath(entity.getPosterPath());
        movieDto.setBackdropPath(entity.getBackdropPath());
        movieDto.setVoteAverage(entity.getVoteAverage());
        movieDto.setVoteCount(entity.getVoteCount());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        movieDto.setReleaseDate(dateFormat.format(entity.getReleaseDate()));
        return movieDto;
    }

    private List<Map.Entry<Long, String>> genreMatch(String entityGenres){
        Map<Long, String> genreMap = new HashMap<>();
        List<String> genreList = Arrays.asList(entityGenres.split(","));
        for (String genre : genreList) {
            long genreId = Long.parseLong(genre);
            genreMap.put(genreId, GenreCached.getGenres().get(genreId));
        }
        List<Map.Entry<Long, String>> collect = genreMap.entrySet().stream().collect(Collectors.toList());
        return collect;
    }

}
