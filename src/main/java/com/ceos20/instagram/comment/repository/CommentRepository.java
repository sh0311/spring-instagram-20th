package com.ceos20.instagram.comment.repository;

import com.ceos20.instagram.comment.domain.Comment;
import com.ceos20.instagram.post.domain.Post;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public class CommentRepository {

    @PersistenceContext
    private EntityManager em;

    public void save(Comment comment) {
        em.persist(comment);
    }

    public Comment findById(String id) {
        Comment comment=em.find(Comment.class, id);
        if(comment==null){
            return null;
        }
        return comment;
    }

    public List<Comment> findAll(){
        return em.createQuery("select c from Comment c", Comment.class).getResultList();
    }

    public List<Comment> findByPost_Id(Long postId){
        return em.createQuery("select c from Comment c where c.post.id=:postId and c.parent.id is null", Comment.class)
                .setParameter("postId", postId)
                .getResultList();
    }




    public List<Comment> findByParent_Id(Long parentId) {
        return em.createQuery("select c from Comment c where c.parent.id=:parentId", Comment.class)
                .setParameter("parentId", parentId)
                .getResultList();
    }
}
