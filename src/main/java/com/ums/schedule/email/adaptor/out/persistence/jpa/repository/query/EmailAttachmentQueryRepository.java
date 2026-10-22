package com.ums.schedule.email.adaptor.out.persistence.jpa.repository.query;

import com.ums.schedule.email.domain.attachment.EmailAttachment;

import java.util.List;
import java.util.UUID;

public interface EmailAttachmentQueryRepository {
    List<EmailAttachment> findListByMessageId(UUID messageId);
}
