package com.ceos20.instagram.reissue.repository;

import com.ceos20.instagram.reissue.domain.Refresh;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshRepository extends JpaRepository<Refresh, Long> {
    Optional<Refresh> findByRefresh(String refresh);
}
