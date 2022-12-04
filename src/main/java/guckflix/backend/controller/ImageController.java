package guckflix.backend.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.CacheControl;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;

@RestController
@Slf4j
@RequiredArgsConstructor
public class ImageController {

    /**
     * 배우 이미지 출력
     */
    @ResponseBody
    @GetMapping("/image/{movie_id}/actors")
    public ResponseEntity<String> image() {
        CacheControl cacheControl = CacheControl.maxAge(Duration.ofDays(365));

        return null;
    }

    @ResponseBody
    @GetMapping("/images/{filename}")
    public Resource downloadImage(@PathVariable String filename) throws
            MalformedURLException {
        return new UrlResource("file:" + fileStore.getFullPath(filename));
    }
}
