package com.ceos20.instagram.user.domain;


import com.ceos20.instagram.follow.domain.Follow;
import com.ceos20.instagram.global.BaseTimeEntity;
import com.ceos20.instagram.post.domain.Post;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // JPA의 엔티티는 기본 생성자가 반드시 필요함
@Builder
@AllArgsConstructor // Builder는 파라미터 있는 생성자가 필요
@DynamicInsert // Insert 시 지정된 default 값을 적용시킨다
public class User extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="user_id")
    private Long id;

    @NotNull
    @Size(min=1, max=30)
    private String nickname;

    private String username;
    private String phone;
    private String email;

    @NotNull
    private String password;
    private String introduce;
    private String profileImageurl;

    @NotNull
    @Builder.Default
    private boolean isPublic = true;

    @Enumerated(EnumType.STRING)
    private UserStatus status;

    @OneToMany(mappedBy="following")
    private List<Follow> followings=new ArrayList<>();

    @OneToMany(mappedBy="follower")
    private List<Follow> followers=new ArrayList<>();

    @OneToMany(mappedBy="user")
    private List<Post> posts=new ArrayList<>();


}
