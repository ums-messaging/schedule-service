package com.ums.schedule.adapter.api.request.email;

import com.ums.schedule.adapter.api.request.SendRequestCreateRequest;

import java.util.List;

public record EmailSendCreateRequest(
        SendRequestCreateRequest request,
        String convertType,
        String encodingType,
        String title,
        String attachmentNameFormat,
        String downloadNameFormat,
        EmailSecurityPolicyRequest securityPolicy,
        List<EmailAttachmentRequest> attachmentList
        ) {
}
