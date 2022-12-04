package guckflix.backend.repository;

import guckflix.backend.entity.Review;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
public class ReviewRepository {

    @Autowired
    EntityManager em;

    public List<Review> findByMovieId(Long movieId, int offset, int limit) {
        return em.createQuery("select r from Review r where r.movieId = :id", Review.class)
                .setParameter("id", movieId)
                .setMaxResults(limit)
                .setFirstResult(offset)
                .getResultList();
    }

    public Long save(Review review) {
        em.persist(review);
        return review.getId();
    }

    public Review findById(Long id) {
        Review findReview = em.createQuery("select r from Review r where r.id = :id", Review.class)
                .setParameter("id", id)
                .getSingleResult();
        return findReview;
    }
}
