package com.ums.schedule.email.application.send.job.model;

import com.ums.schedule.email.application.message.generator.policy.model.AttachMessage;

import java.util.List;

public record MessageSendData(
    String subject,
    MailBodyMessage bodyMessage,
    List<AttachMessage> attachMessages
) {

    public static MessageSendData of(String subject,
                                     MailBodyMessage bodyMessage,
                                     List<AttachMessage> attachMessages) {
        return new MessageSendData(subject, bodyMessage, attachMessages);
    }
}
