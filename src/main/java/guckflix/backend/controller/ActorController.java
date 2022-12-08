package guckflix.backend.controller;

import guckflix.backend.service.CreditService;
import guckflix.backend.service.MovieService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@Api(tags = {"배우 API"})
@RequiredArgsConstructor
public class ActorController {

    private final MovieService movieService;
    private final CreditService creditService;

}
