package com.ums.schedule.domain.send.email.job;

import com.ums.schedule.domain.message.email.attachment.EmailAttachment;
import com.ums.schedule.domain.request.SendRequest;
import com.ums.schedule.domain.target.SendTarget;
import com.ums.schedule.domain.target.TargetMessage;
import com.ums.schedule.domain.target.message.EmailTargetMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public record EmailSendJob(
        SendJob job,
        String mailFrom,
        String mailFromName,
        String imageDir,
        String elho,
        String topic
) {
    public static EmailSendJob of(SendJob job, String topic) {
        return new EmailSendJob(
                job,
                job.senderKey(),
                "hyejin_company",
                "image",
                "test.com",
                topic
                );
    }
}
