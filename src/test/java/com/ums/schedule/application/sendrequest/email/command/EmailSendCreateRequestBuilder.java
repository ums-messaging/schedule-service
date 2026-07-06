package com.ums.schedule.application.sendrequest.email.command;

import com.ums.schedule.adapter.api.request.SendRequestCreateRequest;
import com.ums.schedule.adapter.api.request.email.EmailAttachmentRequest;
import com.ums.schedule.adapter.api.request.email.EmailSecurityPolicyRequest;

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


}
