package guckflix.backend.service;

import guckflix.backend.dto.ActorDto;
import guckflix.backend.dto.ActorDto.Response;
import guckflix.backend.dto.CreditDto;
import guckflix.backend.entity.Actor;
import guckflix.backend.entity.Credit;
import guckflix.backend.entity.Movie;
import guckflix.backend.exception.NotFoundException;
import guckflix.backend.repository.ActorRepository;
import guckflix.backend.repository.CreditRepository;
import guckflix.backend.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import javax.persistence.NoResultException;
import javax.transaction.Transactional;
import java.util.*;
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
            credit.changeMovie(movieMap.get(credit.getMovie().getId()));
            creditRepository.save(credit);
        }

        return savedId;
    }

    public Response findDetail(Long actorId) {
        try {
            Actor actor = actorRepository.findActorDetailById(actorId);
            return new Response(actor);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("No actor of given id", e);
        }
    }

    @Transactional
    public void delete(Long actorId) {
        Actor actor = actorRepository.findActorDetailById(actorId);
        List<Credit> credits = actor.getCredits();
        for (Credit credit : credits) {
            creditRepository.remove(credit);
        }
        actorRepository.remove(actor);

    }

    @Transactional
    public void update(Long actorId, ActorDto.Update actorUpdafeForm, CreditDto.Update creditUpdateForm) {
        Actor actor = actorRepository.findActorDetailById(actorId);
        actor.updateDetail(actorUpdafeForm);

        List<CreditDto.Update.Form> updateForms = creditUpdateForm.getFormList();

        // ConcurrentModificationException 회피?
        Iterator<Credit> iterator = actor.getCredits().iterator();
        while (iterator.hasNext()) {
            Credit credit = iterator.next();
            iterator.remove();
            creditRepository.remove(credit);
        }

        for (CreditDto.Update.Form updateForm : updateForms) {
            Movie movie = movieRepository.findById(updateForm.getMovieId());

            List<Credit> credits = movie.getCredits();

            int maxOrder = 0;
            for (Credit credit : credits) {
                int order = credit.getOrder();
                if (order > maxOrder) {
                    maxOrder = order;
                }
            }

            Credit newCredit = Credit.builder()
                    .movie(movie)
                    .order(maxOrder)
                    .casting(updateForm.getCasting())
                    .actor(actor)
                    .build();

            creditRepository.save(newCredit);
        }
    }
}
