package com.ums.schedule.application.sendrequest.email.command;

import com.ums.schedule.adapter.api.request.email.request.EmailAttachmentListRequest;
import com.ums.schedule.adapter.api.request.request.SendRequestCreateRequest;
import com.ums.schedule.adapter.api.request.email.request.EmailSecurityPolicyRequest;
import com.ums.schedule.adapter.api.request.email.request.EmailSendCreateRequest;
import com.ums.schedule.adapter.api.sendrequest.email.request.SendRequestCreateRequestBuilder;

public class EmailSendCreateRequestBuilder {
    private SendRequestCreateRequest request;
    private String mailType;
    private String convertType;
    private String senderKey;
    private String title;
    private String fileKeyTemplate;
    private String attachmentName;
    private String downloadName;
    private EmailSecurityPolicyRequest securityPolicy;
    private EmailAttachmentListRequest attachmentList;

    public static EmailSendCreateRequestBuilder builder() {
        return new EmailSendCreateRequestBuilder();
    }

    private EmailSendCreateRequestBuilder() {
        this.request = givenSendRequest();
        this.title = "hello world!";
        this.senderKey = "test@test.com";

    }

    public EmailSendCreateRequestBuilder mailType(String mailType) {
        this.mailType = mailType;
        return this;
    }

    public EmailSendCreateRequestBuilder senderKey(String senderKey) {
        this.senderKey = senderKey;
        return this;
    }
    
    public EmailSendCreateRequestBuilder title(String title) {
        this.title = title;
        return this;
    }
    public EmailSendCreateRequestBuilder convertType(String convertType) {
        this.convertType = convertType;
        return this;
    }
    public EmailSendCreateRequestBuilder attachmentName(String attachmentName) {
        this.attachmentName = attachmentName;
        return this;
    }

    public EmailSendCreateRequestBuilder downloadName(String downloadName) {
        this.downloadName = downloadName;
        return this;
    }
    private SendRequestCreateRequest givenSendRequest() {
        return SendRequestCreateRequestBuilder
                .builder().build();
    }

    public EmailSendCreateRequestBuilder securityPolicy(EmailSecurityPolicyRequest securityPolicy) {
        this.securityPolicy = securityPolicy;
        return this;
    }

    public EmailSendCreateRequestBuilder request(SendRequestCreateRequest request) {
        this.request = request;
        return this;
    }

    public EmailSendCreateRequest build() {
        return new EmailSendCreateRequest(
                request,
                mailType,
                senderKey,
                convertType,
                title,
                fileKeyTemplate,
                attachmentName,
                downloadName,
                securityPolicy,
                attachmentList
        );
    }

}
