package com.ceos20.instagram.user.domain;


import com.ceos20.instagram.follow.domain.Follow;
import com.ceos20.instagram.global.BaseTimeEntity;
import com.ceos20.instagram.post.domain.Post;
import com.ceos20.instagram.user.dto.UserRequestDto;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
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
public class User extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="user_id")
    private Long id;

    @NotNull
    @Size(min=1, max=30)
    @Column(unique = true)
    private String nickname;

    private String username;

    @Email
    private String email;

    @NotNull
    private String password;
    private String introduce;
    private String profileImageurl;

    @NotNull
    @Builder.Default
    private boolean isPublic = true;

    @Enumerated(EnumType.STRING)
    private UserStatus status;  //비활,탈퇴-> INACTIVE

    @Builder.Default
    private int followerCount=0;
    @Builder.Default
    private int followingCount=0;

    @OneToMany(mappedBy="user", cascade = CascadeType.ALL,orphanRemoval = true)
    @Builder.Default
    private List<Post> posts=new ArrayList<>();


    public void increaseFollowingNum() {
        followingCount++;
    }

    public void increaseFollowerNum() {
        followerCount++;
    }

    public void decreaseFollowingNum() {
        followingCount--;
    }

    public void decreaseFollowerNum() {
        followerCount--;
    }

    public void update(UserRequestDto userRequestDto) {
        this.nickname = userRequestDto.getNickname();
        this.username = userRequestDto.getUsername();
        this.email = userRequestDto.getEmail();
        this.introduce = userRequestDto.getIntroduce();
        this.profileImageurl = userRequestDto.getProfileImageurl();
        this.status = userRequestDto.getStatus();
    }
}
