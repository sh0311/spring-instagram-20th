package com.ceos20.instagram.refresh.repository;

import com.ceos20.instagram.refresh.domain.Refresh;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RefreshRepository extends JpaRepository<Refresh, Long> {
    Optional<Refresh> findByRefresh(String refresh);

    List<Refresh> findAllByOrderByCreatedAtAsc();
}
