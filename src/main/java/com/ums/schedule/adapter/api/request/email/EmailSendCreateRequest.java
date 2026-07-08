package com.ums.schedule.adapter.api.request.email;

import com.ums.schedule.adapter.api.request.SendRequestCreateRequest;
import com.ums.schedule.application.template.email.query.model.EmailTemplateDetailQuery;

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

        public EmailTemplateDetailQuery toQuery(String customerId) {
                return EmailTemplateDetailQuery.of(customerId, this);
        }
}
