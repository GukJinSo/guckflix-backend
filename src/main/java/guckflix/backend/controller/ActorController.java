package guckflix.backend.controller;

import guckflix.backend.dto.ActorDto;
import guckflix.backend.dto.ActorDto.Post;
import guckflix.backend.dto.CreditDto;
import guckflix.backend.dto.MemberDto;
import guckflix.backend.dto.MovieDto;
import guckflix.backend.file.FileConst;
import guckflix.backend.file.FileUploader;
import guckflix.backend.service.ActorService;
import guckflix.backend.service.CreditService;
import guckflix.backend.service.MovieService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Slf4j
@RestController
@Api(tags = {"배우 API"})
@RequiredArgsConstructor
public class ActorController {

    private final ActorService actorService;

    private final FileUploader fileUploader;

    @GetMapping("/actors/{actorId}")
    @ApiOperation(value = "배우 조회", notes = "배우를 조회한다")
    public ResponseEntity<ActorDto.Response> getActor(@PathVariable Long actorId) throws BindException {
        return ResponseEntity.ok(actorService.findDetail(actorId));
    }

    @PostMapping("/actors")
    @ApiOperation(value = "배우 등록", notes = "배우를 등록한다")
    public ResponseEntity newActor(@RequestPart ActorDto.Post form,
                                   @RequestPart MultipartFile profileFile) throws BindException {

        String profileUUID = UUID.randomUUID().toString()+".jpg";
        form.setProfilePath(profileUUID);
        actorService.save(form);

        fileUploader.upload(profileFile, FileConst.DIRECTORY_PROFILE, profileUUID);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @DeleteMapping("/actors/{actorId}")
    @ApiOperation(value = "배우 삭제", notes = "배우를 삭제한다. 크레딧도 함께 삭제")
    public ResponseEntity delete(@PathVariable Long actorId){

        ActorDto.Response actor = actorService.findDetail(actorId);
        actorService.delete(actorId);
        fileUploader.delete(FileConst.DIRECTORY_PROFILE, actor.getProfilePath());

        return new ResponseEntity(HttpStatus.OK);
    }

    @PatchMapping("/actors/{actorId}")
    @ApiOperation(value = "배우 수정", notes = "배우 정보와 이미지를 수정한다. 크레딧도 수정 가능")
    public ResponseEntity update(@PathVariable Long actorId,
                                 @RequestPart ActorDto.Update actorUpdafeForm,
                                 @RequestPart MultipartFile w500File,
                                 @RequestPart CreditDto.Update creditUpdateForm
    ){

}
