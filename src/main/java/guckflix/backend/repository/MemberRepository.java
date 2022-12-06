package guckflix.backend.repository;

import guckflix.backend.entity.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
public class MemberRepository {

    @Autowired
    EntityManager em;


    public Member findByUsername(String username) {
        return em.createQuery("select m from Member m where username = :username", Member.class)
                .setParameter("username", username)
                .getSingleResult();
    }

    public void save(Member member) {
        em.persist(member);
    }
}
