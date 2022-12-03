package guckflix.backend.repository;


import guckflix.backend.entity.Movie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
public class MovieRepositoryQuerydsl {

    @Autowired
    private EntityManager em;

    public List<Movie> findSimilarByGenre(Long id, int offset, int limit) {


        return null;
    }
}
