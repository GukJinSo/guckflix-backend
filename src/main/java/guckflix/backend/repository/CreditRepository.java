package guckflix.backend.repository;

import guckflix.backend.entity.Credit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
public class CreditRepository {

    @Autowired
    EntityManager em;

    public List<Credit> findByMovieId(Long movieId) {
        return em.createQuery("select c from Credit c join fetch c.actor where c.movie.id = :id", Credit.class)
                .setParameter("id", movieId)
                .getResultList();
    }
}
