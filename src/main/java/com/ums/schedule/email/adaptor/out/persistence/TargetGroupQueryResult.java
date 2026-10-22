package com.ums.schedule.email.adaptor.out.persistence;

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
