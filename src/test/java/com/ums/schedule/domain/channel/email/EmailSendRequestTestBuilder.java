package com.ums.schedule.domain.channel.email;

import com.ums.schedule.domain.channel.email.message.EmailBody;
import com.ums.schedule.domain.channel.email.message.EmailBodyTestBuilder;
import com.ums.schedule.domain.request.SendRequest;
import com.ums.schedule.domain.target.upload.TargetUpload;

import java.util.UUID;

public class EmailSendRequestTestBuilder {
    private SendRequest sendRequest;
    private EmailBody body = EmailBodyTestBuilder.builder().build();
    private String mailFrom = "jang314@naver.com";
    private String mailFromName = "jang";
    private String emailTemplateKey = UUID.randomUUID().toString();

    public static EmailSendRequestTestBuilder builder() {
        return new EmailSendRequestTestBuilder();
    }

    public EmailSendRequestTestBuilder sendRequest(SendRequest sendRequest) {
        this.sendRequest = sendRequest;
        return this;
    }

    public EmailSendRequestTestBuilder emailBody(EmailBody emailBody) {
        this.body = emailBody;
        return this;
    }

    public EmailSendRequestTestBuilder mailFrom(String mailFrom) {
        this.mailFrom = mailFrom;
        return this;
    }

    public EmailSendRequestTestBuilder mailFromName(String mailFromName) {
        this.mailFromName = mailFromName;
        return this;
    }

    public EmailSendRequestTestBuilder emailTemplateKey(String emailTemplateKey) {
        this.emailTemplateKey = emailTemplateKey;
        return this;
    }

    public EmailSendRequest build() {

        return new EmailSendRequest(null,
                sendRequest,
                body,
                mailFrom,
                mailFromName,
                emailTemplateKey);
    }
}
