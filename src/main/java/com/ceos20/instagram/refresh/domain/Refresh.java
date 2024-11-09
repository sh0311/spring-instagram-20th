package com.ceos20.instagram.refresh.domain;

import com.ceos20.instagram.global.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Refresh extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="refresh_id")
    private Long id;

    private String username;
    private Long userId;
    private String refresh;
    private String expiration;
}
