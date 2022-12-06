package guckflix.backend.repository;

import guckflix.backend.entity.Credit;
import guckflix.backend.entity.Movie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
public class CreditRepository implements CommonRepository<Credit, Long> {

    @Autowired
    EntityManager em;

    @Override
    public Long save(Credit entity){
        if(entity.getId() == null){
            em.persist(entity);
        } else {
            em.merge(entity);
        }
        return entity.getId();
    }

    @Override
    public Credit findById(Long id) {
        return em.find(Credit.class, id);
    }

    @Override
    public void delete(Credit entity){
        em.remove(entity);
    }

    public List<Credit> findByMovieId(Long movieId) {
        return em.createQuery("select c from Credit c join fetch c.actor where c.movie.id = :id", Credit.class)
                .setParameter("id", movieId)
                .getResultList();
    }
}
