package com.ums.schedule.application.sendrequest.email.command;

import com.ums.schedule.adapter.api.request.request.SendRequestCreateRequest;
import com.ums.schedule.adapter.api.request.email.request.EmailAttachmentRequest;
import com.ums.schedule.adapter.api.request.email.request.EmailSecurityPolicyRequest;
import com.ums.schedule.adapter.api.request.email.request.EmailSendCreateRequest;
import com.ums.schedule.adapter.api.sendrequest.email.request.SendRequestCreateRequestBuilder;

import java.util.List;

public class EmailSendCreateRequestBuilder {
    private SendRequestCreateRequest request;
    private String convertType;
    private String senderKey;
    private String title;
    private String attachmentNameFormat;
    private String downloadNameFormat;
    private EmailSecurityPolicyRequest securityPolicy;
    private List<EmailAttachmentRequest> attachmentList;

    public static EmailSendCreateRequestBuilder builder() {
        return new EmailSendCreateRequestBuilder();
    }

    private EmailSendCreateRequestBuilder() {
        this.request = givenSendRequest();
        this.title = "hello world!";
        this.senderKey = "test@test.com";

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
        this.attachmentNameFormat = attachmentName;
        return this;
    }

    public EmailSendCreateRequestBuilder downloadName(String downloadName) {
        this.downloadNameFormat = downloadName;
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
                senderKey,
                convertType,
                title,
                attachmentNameFormat,
                downloadNameFormat,
                securityPolicy,
                attachmentList
        );
    }

}
