package guckflix.backend.service;

import guckflix.backend.dto.ActorDto;
import guckflix.backend.dto.ActorDto.Response;
import guckflix.backend.entity.Actor;
import guckflix.backend.entity.Credit;
import guckflix.backend.entity.Movie;
import guckflix.backend.repository.ActorRepository;
import guckflix.backend.repository.CreditRepository;
import guckflix.backend.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ActorService {

    private final ActorRepository actorRepository;
    private final CreditRepository creditRepository;
    private final MovieRepository movieRepository;

    @Transactional
    public Long save(ActorDto.Post form) {

        // 저장하려면, 영화를 찾아와야 함
        List<Long> movieIds = form.getCredits().stream().map((creditParams) -> creditParams.getMovieId()).collect(Collectors.toList());
        List<Movie> findMovies = movieRepository.findByIds(movieIds);
        Map<Long, Movie> movieMap = findMovies.stream().collect(Collectors.toMap((ent) -> ent.getId(), (ent) -> ent));

        // 배우 생성해서 넣기
        Actor actor = Actor.builder()
                .profilePath(form.getProfilePath())
                .name(form.getName())
                .build();
        Long savedId = actorRepository.save(actor);

        // 크레딧 생성해서 넣기
        List<Credit> credits = form.getCredits().stream().map((dto) ->
                Credit.builder()
                        .casting(dto.getCasting())
                        .order(dto.getOrder())
                        .actor(actor)
                        .movie(movieMap.get(dto.getMovieId()))
                        .build()
        ).collect(Collectors.toList());

        // 양방향 연관관계 편의 세팅 후에 크레딧 save
        credits.stream().forEach((entity)-> {
            entity.changeActor(actor);
            creditRepository.save(entity);
        });

        return savedId;
    }

    public Response findDetail(Long actorId) {
        Actor actorDetail = actorRepository.findActorDetailById(actorId);
        return new Response(actorDetail);
    }
}
