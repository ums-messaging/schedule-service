package com.ums.schedule.application.message.email.model;

import com.ums.schedule.adapter.api.request.email.EmailSecurityPolicyRequest;
import com.ums.schedule.adapter.api.request.email.EmailSendCreateRequest;
import com.ums.schedule.application.template.email.query.model.EmailTemplateContentResult;
import com.ums.schedule.application.template.email.query.model.EmailTemplateDetailResult;
import com.ums.schedule.common.code.mapper.EnumMapperValue;
import com.ums.schedule.domain.sendrequest.resource.email.code.AttachmentEnumMapper;
import com.ums.schedule.domain.sendrequest.template.email.code.EmailTemplateSectionEnum;

import java.util.List;
import java.util.Map;

public record EmailConvertPolicyCommand(
        String convertType,
        boolean hasSecurityPolicy,
        String encryptionType,
        String passwordHash,
        String passwordFormat,
        String passwordPolicy,
        String permissionMask,
        EmailTemplateContentResult header,
        EmailTemplateContentResult body,
        EmailTemplateContentResult cover,
        EmailTemplateContentResult footer,
        List<EmailTemplateContentResult> attachmentList
) {

    public static EmailConvertPolicyCommand of(EmailSendCreateRequest request, EmailTemplateDetailResult template) {
        Map<EmailTemplateSectionEnum, EmailTemplateContentResult> templateMap = template.getHeaderFooter();
        EmailSecurityPolicyRequest securityPolicy = request.securityPolicy();
        if(securityPolicy != null) {
            return EmailConvertPolicyCommand.of(request, securityPolicy, templateMap, template);
        }
        return new EmailConvertPolicyCommand(
                request.convertType(),
                true,
                null,
                null,
                null,
                null,
                null,
                templateMap.get(EmailTemplateSectionEnum.HEADER),
                template.getBody(),
                templateMap.get(EmailTemplateSectionEnum.COVER),
                templateMap.get(EmailTemplateSectionEnum.FOOTER),
                template.getAttachmentList()
        );
    }

    private static EmailConvertPolicyCommand of(EmailSendCreateRequest request, EmailSecurityPolicyRequest securityPolicy, Map<EmailTemplateSectionEnum, EmailTemplateContentResult> templateMap, EmailTemplateDetailResult template) {
        return new EmailConvertPolicyCommand(
                request.convertType(),
                true,
                securityPolicy.encryptionType(),
                securityPolicy.passwordHash(),
                securityPolicy.passwordFormat(),
                securityPolicy.passwordPolicy(),
                securityPolicy.permissionMask(),
                templateMap.get(EmailTemplateSectionEnum.HEADER),
                template.getBody(),
                templateMap.get(EmailTemplateSectionEnum.COVER),
                templateMap.get(EmailTemplateSectionEnum.FOOTER),
                template.getAttachmentList()
        );
    }



    public SecurityPolicyCommand toSecurityPolicyCommand(Map<AttachmentEnumMapper, EnumMapperValue> securityPolicyMap) {
        if(hasSecurityPolicy) {
            return SecurityPolicyCommand.of(this, securityPolicyMap);
        }
        return null;
    }
}
