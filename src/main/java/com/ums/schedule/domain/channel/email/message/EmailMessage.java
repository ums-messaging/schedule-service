package com.ums.schedule.domain.channel.email.message;

import com.ums.schedule.domain.channel.email.EmailSendRequest;
import com.ums.schedule.domain.target.upload.TargetUpload;

public record EmailMessage(
        TargetUpload targetUpload,
        EmailTemplate template,
        EmailBody body
) {
    public static EmailMessage of(TargetUpload targetUpload,
                                  EmailTemplate template, EmailSendRequest request) {
        return new EmailMessage(targetUpload, template, request.getBody());
    }
}
