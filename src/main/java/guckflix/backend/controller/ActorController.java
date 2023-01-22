package guckflix.backend.controller;

import guckflix.backend.dto.ActorDto;
import guckflix.backend.dto.ActorDto.Post;
import guckflix.backend.dto.MemberDto;
import guckflix.backend.service.ActorService;
import guckflix.backend.service.CreditService;
import guckflix.backend.service.MovieService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@Api(tags = {"배우 API"})
@RequiredArgsConstructor
public class ActorController {

    private final ActorService actorService;

    @GetMapping("/actors/{actorId}")
    @ApiOperation(value = "배우 조회", notes = "배우를 조회한다")
    public ResponseEntity<ActorDto.Response> getActor(@PathVariable Long actorId) throws BindException {
        return ResponseEntity.ok(actorService.findDetail(actorId));
    }

    @PostMapping("/actors")
    @ApiOperation(value = "배우 등록", notes = "배우를 등록한다")
    public ResponseEntity newActor(@Validated @ModelAttribute Post form, BindingResult br) throws BindException {
        if (br.hasErrors()) {
            throw new BindException(br);
        }
        actorService.save(form);
        return ResponseEntity.ok("OK");
    }

}
