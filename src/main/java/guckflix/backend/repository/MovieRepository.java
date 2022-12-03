package guckflix.backend.repository;

import guckflix.backend.entity.Movie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

import static guckflix.backend.config.QueryWeight.*;


@Repository
public class MovieRepository {

    @Autowired
    private EntityManager em;

    public void save(Movie entity){
        em.persist(entity);
    }

    public List<Movie> findPopular(int offset, int limit){
        return em.createQuery("select m from Movie m order by m.popularity desc", Movie.class)
                .setFirstResult(offset) // offset
                .setMaxResults(limit)
                .getResultList();
    }


    /**
     * select title, vote_average, vote_count, popularity from movie
     * order by ( vote_average * 가중치 ) + (vote_count * 가중치 ) + (popularity * 가중치 ) desc;
     */
    public List<Movie> findTopRated(int offset, int limit) {
        return em.createQuery("select m from Movie m" +
                        " order by ((m.voteAverage * :voteAverage) + (m.voteCount * :voteCount) + (popularity * :popularity )) desc", Movie.class)
                .setParameter("voteAverage", VOTE_AVERAGE)
                .setParameter("voteCount", VOTE_COUNT)
                .setParameter("popularity", POPULARITY)
                .setFirstResult(offset) // offset
                .setMaxResults(limit)
                .getResultList();
    }

    /**
     * @param id
     * @return
     * @throws EmptyResultDataAccessException
     */
    public Movie findById(Long id) throws EmptyResultDataAccessException {
        Movie movie = em.createQuery("select m from Movie m where m.id = :id", Movie.class)
                .setParameter("id", id)
                .getSingleResult();
        return movie;
    }

}
