package com.ums.schedule.adapter.persistence;

import com.ums.schedule.domain.target.message.AttachmentPayload;

import java.util.List;

public record TargetGroupQueryResult(
        Long id,
        Long groupId,
        String domain,
        String targetKey,
        String targetName,
        String contact,
        String subject,
        String headerMessage,
        String bodyMessage,
        String footerMessage,
        String attachments
) {
}
