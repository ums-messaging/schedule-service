package com.ums.schedule.application.message.email;

import com.ums.schedule.application.message.email.model.AttachmentPipelineCommand;

public record EmailResourceCommand(
        String attachmentName,
        String downloadName,
        String objectKey,
        String password
) {
    public static EmailResourceCommand of(AttachmentPipelineCommand command) {
        return new EmailResourceCommand(
                command.attachmentName(),
                command.downloadName(),
                command.objectKey(),
                command.userPassword()
        );
    }
}
