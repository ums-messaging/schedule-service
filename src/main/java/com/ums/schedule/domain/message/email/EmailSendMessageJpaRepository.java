package com.ums.schedule.domain.message.email;

import com.ums.schedule.domain.message.email.EmailSendMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface EmailSendMessageJpaRepository extends JpaRepository<EmailSendMessage, UUID> {
}
