package com.ceos20.instagram.dm.repository;

import com.ceos20.instagram.dm.domain.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {
}
