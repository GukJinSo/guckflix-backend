package guckflix.backend.service;

import guckflix.backend.dto.CreditDto;
import guckflix.backend.dto.CreditDto.Response;
import guckflix.backend.entity.Credit;
import guckflix.backend.repository.CreditRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CreditService {

    private final CreditRepository creditRepository;

    public List<Response> findActors(Long movieId) {
        return creditRepository.findByMovieId(movieId).stream()
                .map((entity)-> new Response(entity))
                .collect(Collectors.toList());
    }

}
