package guckflix.backend.repository;

import guckflix.backend.entity.Video;
import guckflix.backend.entity.enums.ISO639;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Locale;

@Repository
public class VideoRepository {

    @Autowired EntityManager em;

    public List<Video> findByMovieId(Long movieId, String locale){
        return em.createQuery("select v from Video v where v.movie.id = :id and v.iso639 = :locale", Video.class)
                .setParameter("id", movieId)
                .setParameter("locale", ISO639.valueOf(locale))
                .getResultList();
    }
}
