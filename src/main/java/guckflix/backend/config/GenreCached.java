package guckflix.backend.config;

import guckflix.backend.entity.Genre;
import guckflix.backend.repository.GenreRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class GenreCached {

    private final GenreRepository repository;

    @Getter
    private static Map<Long, String> genres;

    @PostConstruct
    public void init(){
        Map<Long, String> genresMap = new HashMap<>();
        List<Genre> genreList = repository.findAll();
        for (Genre genre : genreList) {
            genresMap.put(genre.getId(), genre.getGenreName());
        }
        genres = genresMap;
    }

}
