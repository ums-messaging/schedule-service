package com.ums.schedule.domain.message.email.attachment;

import com.ums.schedule.domain.message.email.attachment.EmailAttachment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailAttachmentJpaRepository extends JpaRepository<EmailAttachment, Long> {
}
