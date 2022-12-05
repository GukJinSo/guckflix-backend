package guckflix.backend.repository;

import guckflix.backend.dto.request.PagingRequest;
import guckflix.backend.dto.response.wrapper.Paging;
import guckflix.backend.entity.Review;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
public class ReviewRepository {

    @Autowired
    EntityManager em;

    public Paging<Review> findByMovieId(Long movieId, PagingRequest pagingRequest) {
        List<Review> list = em.createQuery("select r from Review r where r.movieId = :id", Review.class)
                .setParameter("id", movieId)
                .setFirstResult(pagingRequest.getOffset())
                .setMaxResults(pagingRequest.getLimit())
                .getResultList();

        int totalCount = em.createQuery("select r from Review r where r.movieId = :id", Long.class).getSingleResult().intValue();
        int totalPage = pagingRequest.getTotalPage(totalCount, pagingRequest.getLimit());
        return new Paging(pagingRequest.getRequestPage(), list, totalCount, totalPage, pagingRequest.getLimit());
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
