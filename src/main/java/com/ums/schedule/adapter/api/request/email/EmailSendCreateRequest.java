package com.ums.schedule.adapter.api.request.email;

import com.ums.schedule.adapter.api.request.SendRequestCreateRequest;
import com.ums.schedule.application.ums.email.template.query.model.EmailTemplateDetailQuery;
import com.ums.schedule.common.util.ValueResolverUtils;
import com.ums.schedule.domain.sendrequest.resource.email.code.PasswordTypeEnum;
import org.springframework.util.StringUtils;

import java.util.*;

public record EmailSendCreateRequest(
        SendRequestCreateRequest request,
        String convertType,
        String encodingType,
        String title,
        String attachmentNameFormat,
        String downloadNameFormat,
        EmailSecurityPolicyRequest securityPolicy,
        List<EmailAttachmentRequest> attachmentList
        )
{
        public EmailTemplateDetailQuery toQuery(String customerId) {
                return EmailTemplateDetailQuery.of(customerId, this);
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

        public Map<PasswordTypeEnum, String> toPasswordTypeMap(String defaultPasswordPolicy) {
                Map<PasswordTypeEnum, String> passwordTypeMap = new EnumMap<>(PasswordTypeEnum.class);
                toPutMap(passwordTypeMap, PasswordTypeEnum.PASSWORD_POLICY, ValueResolverUtils.getValueOrDefault(passwordPolicy(), defaultPasswordPolicy));
                toPutMap(passwordTypeMap, PasswordTypeEnum.PASSWORD_POLICY, passwordFormat());
                return passwordTypeMap;
        }

        private void toPutMap(Map<PasswordTypeEnum, String> passwordTypeMap, PasswordTypeEnum passwordType, String putValue) {
                if(StringUtils.hasText(putValue)) {
                        passwordTypeMap.put(passwordType, putValue);
                }
        }
}
