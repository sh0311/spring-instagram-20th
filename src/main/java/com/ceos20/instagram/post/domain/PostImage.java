package com.ceos20.instagram.post.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class PostImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="post_image_id")
    private Long id;

    private String postImageurl;
    private String originalFileName;

    //하나의 게시글 내에서 이미지 순서를 나타냄(사용자가 지정)
    private int imageOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="post_id")
    private Post post;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PostImage postImage = (PostImage) o;
        return Objects.equals(id, postImage.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
