package guckflix.backend.service;

import guckflix.backend.config.GenreCached;
import guckflix.backend.dto.CreditDto;
import guckflix.backend.dto.GenreDto;
import guckflix.backend.dto.MovieDto;
import guckflix.backend.dto.MovieDto.Response;
import guckflix.backend.dto.paging.PagingRequest;
import guckflix.backend.dto.paging.Paging;
import guckflix.backend.dto.paging.Slice;
import guckflix.backend.entity.*;
import guckflix.backend.exception.NotFoundException;
import guckflix.backend.repository.ActorRepository;
import guckflix.backend.repository.CreditRepository;
import guckflix.backend.repository.GenreRepository;
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
    private final GenreRepository genreRepository;

    public Response findById(Long id){
        Movie findMovie = movieRepository.findById(id);
        if (findMovie == null) {
            throw new NotFoundException("No movie of given id");
        }
        return new Response(findMovie);
    }

    public Paging<Response> findSimilar(Long id, PagingRequest pagingRequest) {
        Movie findMovie = movieRepository.findByIdFetch(id);

        Paging<Movie> similar = movieRepository.findSimilarByGenres(findMovie, pagingRequest);
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
    public Long save(Post post) {

        List<Long> ids = post.getCredits().stream().map(dto -> dto.getActorId()).collect(Collectors.toList());

        Movie movie = createMovie(post);
        createMovieGenres(post, movie);

        List<Actor> actors = actorRepository.findAllByIds(ids);
        createCredit(post.getCredits(), actors, movie);

        return movieRepository.save(movie);
    }

    private List<Credit> createCredit(List<CreditDto.Post> creditDtos, List<Actor> actors, Movie movie) {

        List<Credit> credits = new ArrayList<>();

        int maxOrder = 0;
        for (CreditDto.Post creditDto : creditDtos) {
            for (Actor actor : actors) {
                if(actor.getId().equals(creditDto.getActorId())) {
                    Credit credit = Credit.builder()
                            .casting(creditDto.getCasting())
                            .movie(null)
                            .actor(null)
                            .order(maxOrder++)
                            .build();
                    credits.add(credit);
                    credit.changeMovie(movie);
                    credit.changeActor(actor);
                    creditRepository.save(credit);
                }
            }
        }
        return credits;
    }

    private Movie createMovie(Post post) {
        Movie movie = Movie.builder()
                .title(post.getTitle())
                .backdropPath(post.getBackdropPath())
                .posterPath(post.getPosterPath())
                .overview(post.getOverview())
                .releaseDate(post.getReleaseDate())
                .credits(new ArrayList<>())
                .videos(new ArrayList<>())
                .movieGenres(new ArrayList<>())
                .build();
        return movie;
    }

    private List<MovieGenre> createMovieGenres(MovieDto.Post post, Movie movie) {
        List<MovieGenre> movieGenres = new ArrayList<>();
        List<Genre> genres = genreRepository.findAll();
        for (GenreDto dto : post.getGenres()) {
            for (Genre genre : genres) {
                if(dto.getId().equals(genre.getId())){
                    MovieGenre movieGenre = MovieGenre.builder().genre(genre)
                            .movie(null).build();
                    movieGenre.changeMovie(movie);
                    movieGenres.add(movieGenre);
                }
            }
        }
        return movieGenres;
    }

    @Transactional
    public void update(MovieDto.Update movieUpdateForm, Long movieId){

        Movie movie = movieRepository.findById(movieId);

        // 영화에 걸려있는 Credit 삭제
        for (Credit credit : movie.getCredits()) {
            creditRepository.remove(credit);
        }

        // 새 Credit 생성
        List<Credit> credits = new ArrayList<>();
        int index = 0;
        for (CreditDto.Post form : movieUpdateForm.getCredits()) {
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
