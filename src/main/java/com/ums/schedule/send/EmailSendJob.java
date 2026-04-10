package com.ums.schedule.send;

import com.ums.schedule.send.domain.request.EmailBody;
import com.ums.schedule.send.domain.request.EmailSendRequest;
import com.ums.schedule.send.domain.request.SendRequest;
import com.ums.schedule.send.domain.request.upload.TargetUpload;
import com.ums.schedule.template.domain.email.EmailTemplate;

public record EmailSendJob(
        TargetUpload targetUpload,
        EmailTemplate template,
        EmailBody body
) {
    public static EmailSendJob of(TargetUpload targetUpload,
                                  EmailTemplate template, EmailSendRequest request) {
        return new EmailSendJob(targetUpload, template, request.getBody());
    }
}
