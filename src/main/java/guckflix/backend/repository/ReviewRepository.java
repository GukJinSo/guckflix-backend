package guckflix.backend.repository;

import guckflix.backend.dto.request.PagingRequest;
import guckflix.backend.dto.response.paging.Paging;
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

        int totalCount = em.createQuery("select count(r) from Review r where r.movieId = :id", Long.class)
                .setParameter("id", movieId)
                .getSingleResult().intValue();
        int totalPage = getTotalPage(totalCount, pagingRequest.getLimit());
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

    public int getTotalPage(int totalCount, int limit){
        int totalPage = totalCount / limit;
        if(totalCount % limit > 0) {
            totalPage = totalPage + 1;
        }
        return totalPage;
    }
}
