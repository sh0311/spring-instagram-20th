package com.ceos20.instagram.user.repository;

import com.ceos20.instagram.post.domain.Post;
import com.ceos20.instagram.user.domain.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public class UserRepository {

    @PersistenceContext
    private EntityManager em;

    public void save(User user) {
        em.persist(user);
    }

    public User findById(String id) {
        User user=em.find(User.class, id);
        if(user==null){
            return null;
        }
        return user;
    }

    public List<User> findAll(){
        return em.createQuery("select u from User u", User.class).getResultList();
    }


    public void delete(User user) {
        em.remove(user);
    }

}
