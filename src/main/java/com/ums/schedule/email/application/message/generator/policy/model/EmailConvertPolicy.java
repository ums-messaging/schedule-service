package com.ums.schedule.email.application.message.generator.policy.model;

import com.ums.schedule.email.domain.common.FileMetadata;

public record EmailConvertPolicy(
        FileMetadata body,
        AttachMessage attachMessage
) {
    public static EmailConvertPolicy of(FileMetadata body, AttachMessage attachMessage) {
        return new EmailConvertPolicy(
                body, attachMessage
        );
    }

    public static EmailConvertPolicy of(FileMetadata body) {
        return new EmailConvertPolicy(body,null);
    }
}
