package com.ceos20.instagram.comment.domain;

import com.ceos20.instagram.global.BaseTimeEntity;
import com.ceos20.instagram.post.domain.Post;
import com.ceos20.instagram.user.domain.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
  // javax.validation 패키지는 기본적으로 포함되지 않으므로 외부 라이브러리를 프로젝트에 포함해야 함. (build.gradle에 validation 의존성 축)

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class Comment extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="comment_id")
    private Long id;

    @Builder.Default
    private int likeNum=0;

    @NotNull
    @Column(columnDefinition = "text")
    private String content;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="post_id")
    private Post post;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="parent_id")
    private Comment parent;

    // 부모댓글 지워질 때 자식댓글들도 함께 지워지게 하려고
    @OneToMany(mappedBy="parent", cascade = CascadeType.ALL,orphanRemoval = true)
    @Builder.Default
    private List<Comment> children = new ArrayList<>();

    public void increaseLike() {
        this.likeNum++;
    }

    public void decreaseLike() {
        if(this.likeNum>0){
            this.likeNum--;
        }
        else{
            this.likeNum=0;
        }
    }
}
