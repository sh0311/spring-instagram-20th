package com.ceos20.instagram.post.repository;

import com.ceos20.instagram.post.domain.Post;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.Optional;

@Repository

public class PostRepository {

    @PersistenceContext
    private EntityManager em;

    public void save(Post post) {
        em.persist(post);
    }

    public Post findById(String id) {
        Post post=em.find(Post.class, id);
        if(post==null){
            return null;
        }
        return post;
    }

    public List<Post> findAll(){
        return em.createQuery("select p from Post p", Post.class).getResultList();
    }

    public List<Post> findByUser_Id(Long userId){
        return em.createQuery("select p from Post p where p.user.id = :userId", Post.class)
                .setParameter("userId", userId)
                .getResultList();
    }

    public void delete(Post post) {
        em.remove(post);
    }



}
