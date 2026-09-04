package com.ums.schedule.domain.message.email.attachment;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface EmailAttachmentJpaRepository extends JpaRepository<EmailAttachment, UUID> {
}
