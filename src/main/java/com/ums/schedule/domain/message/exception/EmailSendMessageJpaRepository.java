package com.ums.schedule.domain.message.exception;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface EmailSendMessageJpaRepository extends JpaRepository<EmailSendMessage, UUID> {
}
