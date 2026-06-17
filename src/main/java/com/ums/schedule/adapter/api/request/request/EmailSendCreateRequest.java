package com.ums.schedule.adapter.api.request.request;

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
