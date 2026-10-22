package com.ums.schedule.email.domain.attachment;

import com.ums.schedule.email.adaptor.out.persistence.jpa.entity.EmailAttachmentEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EmailAttachmentRepository {
    void save(EmailAttachmentEntity attachment);
    Optional<EmailAttachment> findById(String id);
    List<EmailAttachment> saveAll(UUID messageId, List<EmailAttachment> attachment);
}
