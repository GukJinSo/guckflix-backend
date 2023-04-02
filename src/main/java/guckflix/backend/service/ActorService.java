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
import java.util.ArrayList;
import java.util.HashMap;
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

        // 배우 생성해서 넣기
        Actor actor = Actor.builder()
                .profilePath(form.getProfilePath())
                .name(form.getName())
                .credits(new ArrayList<>())
                .build();
        Long savedId = actorRepository.save(actor);

        // 크레딧의 order를 생성하기 위해서 각 영화의 가장 큰 order를 뽑아야 함
        Map<Long, Integer> maxOrders = new HashMap<>();
        for (Movie findMovie : findMovies) {
            int maxOrder = 0;
            if(findMovie.getCredits() != null){
                for(Credit credit : findMovie.getCredits()) {
                    if(credit.getOrder() >= maxOrder) {
                        maxOrder = credit.getOrder()+1;
                    }
                }
            }
            maxOrders.put(findMovie.getId(), maxOrder);
        }

        Map<Long, Movie> movieMap = findMovies.stream().collect(Collectors.toMap((entity) -> entity.getId(), (ent) -> ent));

        // 크레딧 생성해서 넣기
        List<Credit> credits = form.getCredits().stream().map((dto) ->
                Credit.builder()
                        .casting(dto.getCasting())
                        .order(maxOrders.get(dto.getMovieId()))
                        .movie(movieMap.get(dto.getMovieId()))
                        .build()
        ).collect(Collectors.toList());

        // 크레딧 save
        for (Credit credit : credits) {
            credit.changeActor(actor);
            System.out.println("credit.getMovie().getTitle() = " + credit.getMovie().getTitle());
            credit.changeMovie(movieMap.get(credit.getMovie().getId()));
            creditRepository.save(credit);
        }

        return savedId;
    }

    public Response findDetail(Long actorId) {
        Actor actor = actorRepository.findActorDetailById(actorId);
        return new Response(actor);
    }
}
