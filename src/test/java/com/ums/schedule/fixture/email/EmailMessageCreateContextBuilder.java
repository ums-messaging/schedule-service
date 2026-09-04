package com.ums.schedule.fixture.email;

import com.ums.schedule.application.ums.email.message.model.EmailMessageCreateContext;
import com.ums.schedule.application.ums.email.security.SecurityMail;
import com.ums.schedule.common.code.email.EmailType;
import com.ums.schedule.domain.message.email.convert.ConvertMail;
import com.ums.schedule.domain.message.email.security.SecurityMailPolicy;

public class EmailMessageCreateContextBuilder {
    private EmailType emailType;
    private String title;
    private String headerKey;
    private String bodyKey;
    private String coverKey;
    private String footerKey;
    private String imageDir;

    private SecurityMail securityMail;
    private ConvertMail convertMail;

    public static EmailMessageCreateContextBuilder builder() {
        return new EmailMessageCreateContextBuilder();
    }

    private EmailMessageCreateContextBuilder() {

    }
    public EmailMessageCreateContextBuilder emailType(EmailType emailType) {
        this.emailType = emailType;
        return this;
    }
    public EmailMessageCreateContextBuilder title(String title) {
        this.title = title;
        return this;
    }

    public EmailMessageCreateContextBuilder headerKey(String headerKey) {
        this.headerKey = headerKey;
        return this;
    }

    public EmailMessageCreateContextBuilder bodyKey(String bodyKey) {
        this.bodyKey = bodyKey;
        return this;
    }

    public EmailMessageCreateContextBuilder coverKey(String coverKey) {
        this.coverKey = coverKey;
        return this;
    }

    public EmailMessageCreateContextBuilder footerKey(String footerKey) {
        this.footerKey = footerKey;
        return this;
    }

    public EmailMessageCreateContextBuilder imageDir(String imageDir) {
        this.imageDir = imageDir;
        return this;
    }

    public EmailMessageCreateContextBuilder convertMail(ConvertMail convertMail) {
        this.convertMail = convertMail;
        return this;
    }

    public EmailMessageCreateContextBuilder securityMail(SecurityMail securityMail) {
        this.securityMail = securityMail;
        return this;
    }




    public EmailMessageCreateContext build() {
        return new EmailMessageCreateContext(
                emailType,
                title,
                headerKey,
                bodyKey,
                coverKey,
                footerKey,
                imageDir,
                securityMail,
                convertMail
        );
    }
}
