package guckflix.backend.repository;

import guckflix.backend.entity.Actor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
public class ActorRepository implements CommonRepository <Actor, Long>{

    @Autowired
    EntityManager em;

    @Override
    public Long save(Actor entity) {
        em.persist(entity);
        return entity.getId();
    }

    @Override
    public Actor findById(Long id) {
        return em.find(Actor.class, id);
    }

    @Override
    public void remove(Actor entity) {
        em.remove(entity);
    }

    public Actor findActorDetailById(Long id){
        return em.createQuery("select a from Actor a" +
                        " left join fetch a.credits c" +
                        " left join fetch c.movie" +
                        " where a.id = :id", Actor.class)
                .setParameter("id", id)
                .getSingleResult();
    }

    public List<Actor> findAllByIds(List<Long> ids) {
        return em.createQuery("select a from Actor a where id in :ids", Actor.class).setParameter("ids", ids).getResultList();
    }
}
