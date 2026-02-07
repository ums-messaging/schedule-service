package com.ums.schedule.repository;

import com.ums.schedule.domain.SendMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SendMessageRepository extends JpaRepository<SendMessage, Long> {
}
