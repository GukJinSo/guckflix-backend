package guckflix.backend.service;

import guckflix.backend.config.GenreCached;
import guckflix.backend.dto.CreditDto;
import guckflix.backend.dto.MovieDto;
import guckflix.backend.dto.MovieDto.Response;
import guckflix.backend.dto.paging.PagingRequest;
import guckflix.backend.dto.paging.Paging;
import guckflix.backend.dto.paging.Slice;
import guckflix.backend.entity.Actor;
import guckflix.backend.entity.Credit;
import guckflix.backend.entity.Movie;
import guckflix.backend.exception.NotFoundException;
import guckflix.backend.repository.ActorRepository;
import guckflix.backend.repository.CreditRepository;
import guckflix.backend.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

import static guckflix.backend.dto.MovieDto.*;

@Service
@RequiredArgsConstructor
public class MovieService {

    private final MovieRepository movieRepository;
    private final CreditRepository creditRepository;

    private final ActorRepository actorRepository;

    public Response findById(Long id){
        Movie findMovie = movieRepository.findById(id);
        if (findMovie == null) {
            throw new NotFoundException("No movie of given id");
        }
        return new Response(findMovie);
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

    @Transactional
    public Long save(Post movieDto) {
        Movie movie = dtoToEntity(movieDto);
        return movieRepository.save(movie);
    }

    @Transactional
    public void update(List<CreditDto.Post> creditPatchForm, MovieDto.Update movieUpdateForm, Long movieId){

        Movie movie = movieRepository.findById(movieId);

        // 영화에 걸려있는 Credit 삭제
        for (Credit credit : movie.getCredits()) {
            creditRepository.remove(credit);
        }

        // 새 Credit 생성
        List<Credit> credits = new ArrayList<>();
        int index = 0;
        for (CreditDto.Post form : creditPatchForm) {
            Actor actor = actorRepository.findById(form.getActorId());
            Credit credit = Credit.builder()
                    .actor(actor)
                    .movie(movie)
                    .casting(form.getCasting())
                    .order(index++)
                    .build();
            credits.add(credit);
            creditRepository.save(credit);
        }

        // 영화 정보 수정
        movie.updateDetail(movieUpdateForm);

        // Credit 수정
        for (Credit credit : credits){
            movie.updateCredit(credit); // Movie <-> Credit 양방향 연관관계 설정
        }
    }

    private Movie dtoToEntity(Post dto) {
        return Movie.builder()
                .title(dto.getTitle())
                .backdropPath(dto.getBackdropPath())
                .posterPath(dto.getPosterPath())
                .overview(dto.getOverview())
                .genres(GenreCached.genreListToString(dto.getGenres()))
                .releaseDate(dto.getReleaseDate())
                .credits(new ArrayList<>())
                .build();
    }

    @Transactional
    public void delete(Long movieId) {
        Movie movie = movieRepository.findById(movieId);

        // 크레딧 삭제
        List<Credit> credits = movie.getCredits();
        for (Credit credit : credits) {
            creditRepository.remove(credit);
        }

        // 영화 삭제
        movieRepository.remove(movie);

    }
}
