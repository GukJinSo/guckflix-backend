package guckflix.backend.controller;

import guckflix.backend.service.CreditService;
import guckflix.backend.service.MovieService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ActorController {

    private final MovieService movieService;
    private final CreditService creditService;

}
