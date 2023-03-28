package guckflix.backend.config;

import com.fasterxml.jackson.annotation.JsonIgnore;
import guckflix.backend.entity.Genre;
import guckflix.backend.repository.GenreRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 스프링 시작 시 장르 테이블 캐시
 * 장르는 Movie.genres 컬럼에 "14,15,19" 형식으로 String으로 삽입되어 있음
 * 14, 15, 19처럼 3개의 로우로 분리해서 장르 테이블에 조인하면 많은 I/O 발생 + sql 어려움
 * 장르는 정적인 데이터. 따라서 테이블은 따로 두되 어플리케이션 시작 시 캐시된 이 곳에서 조인함
 *
 * unmodifiableMap으로 생성해서 어플리케이션 로딩 시 외에 put 불가
 */
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
        genres = Collections.unmodifiableMap(genresMap);
    }

    @JsonIgnore
    public static List<Map.Entry<Long, String>> genreToListEntry(String entityGenres){

        if(entityGenres == null || entityGenres.equals("")){
            return null;
        }

        Map<Long, String> genreMap = new HashMap<>();
        List<String> genreList = Arrays.asList(entityGenres.split(","));
        for (String genre : genreList) {
            long genreId = Long.parseLong(genre);
            genreMap.put(genreId, GenreCached.getGenres().get(genreId));
        }
        List<Map.Entry<Long, String>> collect = genreMap.entrySet().stream().collect(Collectors.toList());
        return collect;
    }

    @JsonIgnore
    public static String genreToString(List<Map.Entry<Long, String>> genres){

        if(genres == null || genres.size() == 0) {
            return null;
        }

        return genres.stream().map((entry) -> Integer.toString(entry.getKey().intValue()))
                .collect(Collectors.toList())
                .stream().collect(Collectors.joining(","));
    }

}
