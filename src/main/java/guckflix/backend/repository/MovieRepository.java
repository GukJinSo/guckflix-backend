package guckflix.backend.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import guckflix.backend.dto.request.PagingRequest;
import guckflix.backend.dto.response.wrapper.Paging;
import guckflix.backend.entity.Movie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

import static guckflix.backend.config.QueryWeight.*;
import static guckflix.backend.entity.QMovie.movie;


@Repository
public class MovieRepository {

    @Autowired EntityManager em;

    JPAQueryFactory queryFactory;

    public MovieRepository(EntityManager em) {
        this.em = em;
        this.queryFactory = new JPAQueryFactory(em);
    }

    public void save(Movie entity){
        em.persist(entity);
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

    public Paging<Movie> findPopular(PagingRequest pagingRequest) {
        List<Movie> list = em.createQuery("select m from Movie m order by m.popularity desc", Movie.class)
                .setFirstResult(pagingRequest.getOffset()) // offset
                .setMaxResults(pagingRequest.getLimit())
                .getResultList();
        int totalCount = selectCountAll().intValue();
        int totalPage = pagingRequest.getTotalPage(totalCount, pagingRequest.getLimit());
        return new Paging(pagingRequest.getRequestPage(), list, totalCount, totalPage, pagingRequest.getLimit());
    }

    /**
     * select title, vote_average, vote_count, popularity from movie
     * order by ( vote_average * 가중치 ) + (vote_count * 가중치 ) + (popularity * 가중치 ) desc;
     */
    public Paging<Movie> findTopRated(PagingRequest pagingRequest) {
        List<Movie> list = em.createQuery("select m from Movie m" +
                        " order by ((m.voteAverage * :voteAverage) + (m.voteCount * :voteCount) + (popularity * :popularity )) desc", Movie.class)
                .setParameter("voteAverage", VOTE_AVERAGE)
                .setParameter("voteCount", VOTE_COUNT)
                .setParameter("popularity", POPULARITY)
                .setFirstResult(pagingRequest.getOffset()) // offset
                .setMaxResults(pagingRequest.getLimit())
                .getResultList();
        int totalCount = selectCountAll().intValue();
        int totalPage = pagingRequest.getTotalPage(totalCount, pagingRequest.getLimit());
        return new Paging(pagingRequest.getRequestPage(), list, totalCount, totalPage, pagingRequest.getLimit());
    }

    private Long selectCountAll(){
        return em.createQuery("select count(m) from Movie m", Long.class).getSingleResult();
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
        int totalPage = pagingRequest.getTotalPage(totalCount, pagingRequest.getLimit());
        return new Paging(pagingRequest.getRequestPage(), list, totalCount, totalPage, pagingRequest.getLimit());

//        List<BooleanExpression> list = new ArrayList<>();
//        for (int i = 0; i < genres.size(); i++) {
//            BooleanExpression be = movie.genres.contains(genres.get(i));
//        }
//        BooleanExpression[] bes = list.toArray(new BooleanExpression[list.size()]);

    }
}
