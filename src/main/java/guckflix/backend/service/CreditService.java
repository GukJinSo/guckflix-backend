package guckflix.backend.service;

import guckflix.backend.dto.CreditDto;
import guckflix.backend.dto.CreditDto.Response;
import guckflix.backend.entity.Credit;
import guckflix.backend.repository.ActorRepository;
import guckflix.backend.repository.CreditRepository;
import guckflix.backend.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CreditService {

    private final CreditRepository creditRepository;
    private final MovieRepository movieRepository;
    private final ActorRepository actorRepository;

    public List<Response> findActors(Long movieId) {
        return creditRepository.findByMovieId(movieId).stream()
                .map((entity)-> new Response(entity))
                .collect(Collectors.toList());
    }

    public void save(CreditDto.Post cForm) {
        cForm.getFormList().stream().forEach((form)->
            Credit.builder()
                    .actor(actorRepository.findById(form.getId()))
                    .movie(movieRepository.findById(form.getMovieId()))
                    .casting(form.getCasting())
                    .order(form.getOrder()).build()
        );
    }

    public void update(CreditDto.Update uForm){

        for (CreditDto.Update.Form form : uForm.getFormList()) {
            Credit credit = creditRepository.findById(form.getActorId());
            credit.update(form.getOrder(), form.getCasting());
        }
    }

    public void delete(Long creditId) {
        Credit credit = creditRepository.findById(creditId);
        creditRepository.remove(credit);
    }
}
