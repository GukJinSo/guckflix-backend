package guckflix.backend.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.CollectionExpression;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Visitor;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.BooleanOperation;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import guckflix.backend.entity.Credit;
import guckflix.backend.entity.Movie;
import guckflix.backend.entity.QMovie;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.ArrayList;
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

    public List<Movie> findSimilarByGenres(Long id, List<String> genres, int offset, int limit) {

        BooleanBuilder genreCond = new BooleanBuilder();
        for (String genre : genres) {
            genreCond.or(movie.genres.contains(genre));
        }

        return queryFactory.select(movie)
                .from(movie)
                .orderBy(movie.popularity.desc())
                .where(genreCond.andNot(movie.id.eq(id)))
                .offset(offset)
                .limit(limit)
                .fetch();

//        List<BooleanExpression> list = new ArrayList<>();
//        for (int i = 0; i < genres.size(); i++) {
//            BooleanExpression be = movie.genres.contains(genres.get(i));
//        }
//        BooleanExpression[] bes = list.toArray(new BooleanExpression[list.size()]);

    }


}
