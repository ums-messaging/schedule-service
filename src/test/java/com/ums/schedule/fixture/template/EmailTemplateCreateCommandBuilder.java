package com.ums.schedule.fixture.template;

import com.ums.schedule.application.template.email.query.model.EmailAttachmentDetailQuery;
import com.ums.schedule.application.template.email.query.model.EmailTemplateDetailQuery;

import java.util.List;

public class EmailTemplateCreateCommandBuilder {
    private String customerId;
    private String templateKey;
    private String messageType;
    private String title;
    private String attachmentName;
    private String downloadName;
    private List<EmailAttachmentDetailQuery> attachmentKeyList;

    public static EmailTemplateCreateCommandBuilder builder() {
        return new EmailTemplateCreateCommandBuilder();
    }

    private EmailTemplateCreateCommandBuilder() {
        this.customerId = "jang314";
        this.templateKey = "my_template";
        this.messageType = "ADVERTISE";
        this.title = "이메일 템플릿 제목";
        this.attachmentName = "첨부파일 명";
        this.downloadName = "다운로드 명";
    }

    public EmailTemplateCreateCommandBuilder customerId(String customerId) {
        this.customerId = customerId;
        return this;
    }

    public EmailTemplateCreateCommandBuilder templateKey(String templateKey) {
        this.templateKey = templateKey;
        return this;
    }

    public EmailTemplateCreateCommandBuilder messageType(String messageType) {
        this.messageType = messageType;
        return this;
    }

    public EmailTemplateCreateCommandBuilder title(String title) {
        this.title = title;
        return this;
    }

    public EmailTemplateCreateCommandBuilder attachmentName(String attachmentName) {
        this.attachmentName = attachmentName;
        return this;
    }

    public EmailTemplateCreateCommandBuilder downloadName(String downloadName) {
        this.downloadName = downloadName;
        return this;
    }

    public EmailTemplateCreateCommandBuilder attachmentList(List<EmailAttachmentDetailQuery> commands) {
        this.attachmentKeyList = commands;
        return this;
    }

    public EmailTemplateDetailQuery build() {
        return new EmailTemplateDetailQuery(
                customerId,
                templateKey,
                messageType,
                title,
                attachmentName,
                downloadName,
                attachmentKeyList
        );
    }
}
