package com.ums.schedule.domain.sendrequest.message;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SendMessageJpaRepository extends JpaRepository<SendMessage, UUID> {
}
