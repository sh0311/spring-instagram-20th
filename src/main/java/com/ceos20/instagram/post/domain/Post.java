package com.ceos20.instagram.post.domain;

import com.ceos20.instagram.comment.domain.Comment;
import com.ceos20.instagram.global.BaseTimeEntity;
import com.ceos20.instagram.post.dto.PostRequestDto;
import com.ceos20.instagram.user.domain.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.CreationTimestamp;

import javax.swing.plaf.basic.BasicEditorPaneUI;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor // Builder는 파라미터 있는 생성자가 필요
public class Post extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="post_id")
    private Long id;

    @Column(columnDefinition = "text")
    private String content;

    @Builder.Default
    private int likeNum=0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;


    @OneToMany(mappedBy="post",cascade = CascadeType.ALL,orphanRemoval = true)
    @Builder.Default
    private List<PostImage> images=new ArrayList<>();


    public void mapImages(List<PostImage> images) {
        this.images=images;
    }

    public void update(PostRequestDto postRequestDto,List<PostImage> images) {
        this.content=postRequestDto.getContent();
        this.images.addAll(images);
    }

    public void increaseLikeNum(){
        this.likeNum++;
    }

    public void decreaseLikeNum() {
        if(this.likeNum>0){
            this.likeNum--;
        }
        else{
            this.likeNum=0;
        }
    }
}
