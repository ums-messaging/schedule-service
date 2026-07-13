package com.ums.schedule.application.sendrequest.email.command;

import com.ums.schedule.adapter.api.request.SendRequestCreateRequest;
import com.ums.schedule.adapter.api.request.email.EmailAttachmentRequest;
import com.ums.schedule.adapter.api.request.email.EmailSecurityPolicyRequest;
import com.ums.schedule.adapter.api.request.email.EmailSendCreateRequest;

import java.util.List;

public class EmailSendCreateRequestBuilder {
    private SendRequestCreateRequest request;
    private String convertType;
    private String encodingType;
    private String title;
    private String attachmentNameFormat;
    private String downloadNameFormat;
    private EmailSecurityPolicyRequest securityPolicy;
    private List<EmailAttachmentRequest> attachmentList;

    public static EmailSendCreateRequestBuilder builder() {
        return new EmailSendCreateRequestBuilder();
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
                convertType,
                encodingType,
                title,
                attachmentNameFormat,
                downloadNameFormat,
                securityPolicy,
                attachmentList
        );
    }

}
