package com.ceos20.instagram.user.domain;


import com.ceos20.instagram.follow.domain.Follow;
import com.ceos20.instagram.post.domain.Post;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // JPA의 엔티티는 기본 생성자가 반드시 필요함
@Builder
@AllArgsConstructor // Builder는 파라미터 있는 생성자가 필요
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="user_id")
    private Long id;


    private String nickname;
    private String username;
    private String phone;
    private String email;
    private String password;
    private String introduce;
    private String profile_image_url;
    private boolean isPublic;

    @OneToMany(mappedBy="following")
    private List<Follow> followings=new ArrayList<>();

    @OneToMany(mappedBy="follower")
    private List<Follow> followers=new ArrayList<>();

    @OneToMany(mappedBy="user")
    private List<Post> posts=new ArrayList<>();


}
