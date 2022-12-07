package guckflix.backend.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import guckflix.backend.dto.request.PagingRequest;
import guckflix.backend.dto.response.paging.Paging;
import guckflix.backend.dto.response.paging.Slice;
import guckflix.backend.entity.Movie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

import static guckflix.backend.config.QueryWeight.*;
import static guckflix.backend.entity.QMovie.movie;


@Repository
public class MovieRepository implements CommonRepository<Movie, Long> {

    @Autowired EntityManager em;

    JPAQueryFactory queryFactory;

    public MovieRepository(EntityManager em) {
        this.em = em;
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Long save(Movie entity){
        em.persist(entity);
        em.flush();
        return entity.getId();
    }

    @Override
    public Movie findById(Long id) {
        return em.find(Movie.class, id);
    }

    @Override
    public void remove(Movie entity){
        em.remove(entity);
    }

    public Paging<Movie> findPopular(PagingRequest pagingRequest) {
        List<Movie> list = em.createQuery("select m from Movie m order by m.popularity desc", Movie.class)
                .setFirstResult(pagingRequest.getOffset()) // offset
                .setMaxResults(pagingRequest.getLimit())
                .getResultList();
        int totalCount = selectCountAll().intValue();
        int totalPage = getTotalPage(totalCount, pagingRequest.getLimit());
        return new Paging(pagingRequest.getRequestPage(), list, totalCount, totalPage, pagingRequest.getLimit());
    }

    /**
     * select title, vote_average, vote_count, popularity from movie
     * order by ( vote_average * 가중치 ) + (vote_count * 가중치 ) + (popularity * 가중치 ) desc;
     */
    public Paging<Movie> findTopRated(PagingRequest pagingRequest) {
        List<Movie> list = em.createQuery("select m from Movie m" +
                        " order by ((m.voteAverage * :voteAverage) + (m.voteCount * :voteCount) + (popularity * :popularity )) desc", Movie.class)
                .setParameter("voteAverage", VOTE_AVERAGE_WEIGHT)
                .setParameter("voteCount", VOTE_COUNT_WEIGHT)
                .setParameter("popularity", POPULARITY_WEIGHT)
                .setFirstResult(pagingRequest.getOffset()) // offset
                .setMaxResults(pagingRequest.getLimit())
                .getResultList();
        int totalCount = selectCountAll().intValue();
        int totalPage = getTotalPage(totalCount, pagingRequest.getLimit());
        return new Paging(pagingRequest.getRequestPage(), list, totalCount, totalPage, pagingRequest.getLimit());
    }

    private Long selectCountAll(){
        return em.createQuery("select count(m) from Movie m", Long.class).getSingleResult();
    }

    public Slice<Movie> findByKeyword(String keyword, PagingRequest pagingRequest) {
        List<Movie> list = em.createQuery("select m from Movie m where m.title like :keyword", Movie.class)
                .setParameter("keyword", "%"+keyword+"%")
                .setFirstResult(pagingRequest.getOffset())
                .setMaxResults(pagingRequest.getLimit()+1)
                .getResultList();

        /**
         * Slice는 limit보다 한 개 더 가져와서 다음 페이지가 있는지 확인함
         */
        boolean hasNext = list.size() > pagingRequest.getLimit() ? true : false;
        if (hasNext == true) list.remove(list.size()-1);
        return new Slice<>(hasNext, pagingRequest.getRequestPage(), list, pagingRequest.getLimit());
    }

    public Paging<Movie> findSimilarByGenres(Long id, List<String> genres, PagingRequest pagingRequest) {

        BooleanBuilder genreCond = new BooleanBuilder();
        for (String genre : genres) {
            genreCond.or(movie.genres.contains(genre));
        }

        List<Movie> list = queryFactory.select(movie)
                .from(movie)
                .orderBy(movie.popularity.desc())
                .where(genreCond.andNot(movie.id.eq(id)))
                .offset(pagingRequest.getOffset())
                .limit(pagingRequest.getLimit())
                .fetch();


        int totalCount = selectCountAll().intValue();
        int totalPage = getTotalPage(totalCount, pagingRequest.getLimit());
        return new Paging(pagingRequest.getRequestPage(), list, totalCount, totalPage, pagingRequest.getLimit());

    }

    public int getTotalPage(int totalCount, int limit){
        int totalPage = totalCount / limit;
        if(totalCount % limit > 0) {
            totalPage = totalPage + 1;
        }
        return totalPage;
    }

}
