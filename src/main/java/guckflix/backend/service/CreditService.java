package guckflix.backend.service;

import guckflix.backend.dto.CreditDto;
import guckflix.backend.entity.Credit;
import guckflix.backend.repository.CreditRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CreditService {

    private final CreditRepository creditRepository;

    public List<CreditDto> findActors(Long movieId) {
        List<CreditDto> dtos = new ArrayList<>();
        List<Credit> credits = creditRepository.findByMovieId(movieId);
        for (Credit credit : credits) {
            CreditDto dto = creditEntityToDto(credit);
            dtos.add(dto);
        }
        return dtos;
    }

    private CreditDto creditEntityToDto(Credit entity){
        CreditDto creditDto = new CreditDto();
        creditDto.setId(entity.getActor().getId());
        creditDto.setName(entity.getActor().getName());
        creditDto.setCasting(entity.getCasting());
        creditDto.setOrder(entity.getOrder());
        creditDto.setProfilePath(entity.getActor().getProfilePath());
        return creditDto;
    }
}
