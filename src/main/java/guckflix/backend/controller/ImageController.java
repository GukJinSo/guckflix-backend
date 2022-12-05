package guckflix.backend.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.MalformedURLException;

import static guckflix.backend.config.logic.FileConst.*;
import static org.springframework.http.MediaType.IMAGE_JPEG;

@RestController
@Slf4j
@RequiredArgsConstructor
public class ImageController {

    /**
     * 영화, 배우 이미지 출력
     * ETag로 브라우저 캐시 사용 유도
     */
    @GetMapping(value = "/images/{imageCategory}/{file}")
    public ResponseEntity<Resource> imageVideo(@PathVariable String file,
                                         @PathVariable String imageCategory) throws MalformedURLException {
        Resource resource = null;
        if(imageCategory.equals(DIRECTORY_W500)){
            resource = new UrlResource("file", IMAGE_DIRECTORY_ROOT +"/"+ DIRECTORY_W500 +"/"+file);
        } else if(imageCategory.equals(DIRECTORY_ORIGINAL)) {
            resource = new UrlResource("file", IMAGE_DIRECTORY_ROOT +"/"+ DIRECTORY_ORIGINAL +"/"+file);
        } else if(imageCategory.equals(DIRECTORY_PROFILE)){
            resource = new UrlResource("file", IMAGE_DIRECTORY_ROOT +"/"+ DIRECTORY_PROFILE +"/"+file);
        }
        return ResponseEntity.ok().contentType(IMAGE_JPEG).body(resource);
    }

}
