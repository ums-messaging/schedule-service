package com.ums.schedule.application.ums.email.attachment.model;

import com.ums.schedule.domain.message.email.attachment.EmailAttachment;
import org.springframework.util.StringUtils;

import java.util.Optional;
import java.util.UUID;

public record AttachmentCreateResult(
        UUID id,
        String fileKey
) {
    public static AttachmentCreateResult of(EmailAttachment attachment) {
        return new AttachmentCreateResult(
                attachment.getId(),
                Optional.ofNullable(attachment.getFileKey())
                        .filter(StringUtils::hasText)
                        .orElseGet(() -> attachment.getFileKey())
        );
    }
}
