package guckflix.backend.repository;

import guckflix.backend.entity.Genre;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
public class GenreRepository {

    @Autowired
    EntityManager em;

    public List<Genre> findAll(){
        return em.createQuery("select g from Genre g", Genre.class).getResultList();
    }
}
