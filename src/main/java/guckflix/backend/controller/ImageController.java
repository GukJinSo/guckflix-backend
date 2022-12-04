package guckflix.backend.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.MalformedURLException;

import static guckflix.backend.config.FileConst.*;
import static org.springframework.http.MediaType.IMAGE_JPEG;

@RestController
@Slf4j
@RequiredArgsConstructor
public class ImageController {

    /**
     * 영화, 배우 이미지 출력
     * ETag 비교로 브라우저 캐시 사용 유도
     */
    @GetMapping(value = "/images/{imageSize}/{file}")
    public ResponseEntity<Resource> imageVideo(@PathVariable String file,
                                         @PathVariable String imageSize) throws MalformedURLException {
        Resource resource = null;
        if(imageSize.equals(W500)){
            resource = new UrlResource("file", DIRECTORY_IMAGE_ROOT+"/"+W500+"/"+file);
        } else if(imageSize.equals(ORIGINAL)) {
            resource = new UrlResource("file", DIRECTORY_IMAGE_ROOT+"/"+ORIGINAL+"/"+file);
        } else if(imageSize.equals(W500_PROFILE)){
            resource = new UrlResource("file", DIRECTORY_IMAGE_ROOT+"/"+W500_PROFILE+"/"+file);
        }
        return ResponseEntity.ok().contentType(IMAGE_JPEG).body(resource);
    }

}
