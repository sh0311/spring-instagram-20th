package com.ceos20.instagram.user.repository;

import com.ceos20.instagram.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
