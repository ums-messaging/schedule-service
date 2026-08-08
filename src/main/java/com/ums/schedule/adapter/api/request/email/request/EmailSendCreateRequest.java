package com.ums.schedule.adapter.api.request.email.request;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.ums.schedule.adapter.api.request.request.SendRequestCreateRequest;
import com.ums.schedule.application.ums.email.template.query.model.EmailAttachmentDetailQuery;
import com.ums.schedule.application.ums.email.template.query.model.EmailTemplateDetailQuery;
import com.ums.schedule.common.util.ValueResolverUtils;
import com.ums.schedule.common.code.email.security.PasswordType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.util.StringUtils;

import java.util.*;

public record EmailSendCreateRequest(
        @Valid
        @JsonUnwrapped
        SendRequestCreateRequest request,
        String mailType,
        @NotBlank(message = "SEND_REQUEST:SENDER_KEY_REQUIRED")
        @Email(message = "EMAIL_SEND_REQUEST:INVALID_SENDER_EMAIL")
        String senderKey,
        String convertType,
        @NotBlank(message = "EMAIL_SEND_REQUEST:TITLE_REQUIRED")
        String title,
        String attachmentName,
        String downloadName,
        String fileKeyTemplate,
        @Valid
        EmailSecurityPolicyRequest securityPolicy,
        @Valid
        EmailAttachmentListRequest attachmentList
        )
{
        public EmailTemplateDetailQuery toQuery(String customerId, List<EmailAttachmentDetailQuery> queries) {
                return EmailTemplateDetailQuery.of(customerId, this, queries);
        }

        public String passwordPolicy() {
                return Optional.ofNullable(securityPolicy)
                        .map(EmailSecurityPolicyRequest::passwordPolicy)
                        .orElse(null);
        }

        public String passwordFormat() {
                return Optional.ofNullable(securityPolicy)
                        .map(EmailSecurityPolicyRequest::passwordFormat)
                        .orElse(null);
        }

        public Map<PasswordType, String> toPasswordTypeMap(String defaultPasswordPolicy) {
                Map<PasswordType, String> passwordTypeMap = new EnumMap<>(PasswordType.class);
                toPutMap(passwordTypeMap, PasswordType.PASSWORD_POLICY, ValueResolverUtils.getValueOrDefault(passwordPolicy(), defaultPasswordPolicy));
                toPutMap(passwordTypeMap, PasswordType.PASSWORD_POLICY, passwordFormat());
                return passwordTypeMap;
        }

        private void toPutMap(Map<PasswordType, String> passwordTypeMap, PasswordType passwordType, String putValue) {
                if(StringUtils.hasText(putValue)) {
                        passwordTypeMap.put(passwordType, putValue);
                }
        }
}
