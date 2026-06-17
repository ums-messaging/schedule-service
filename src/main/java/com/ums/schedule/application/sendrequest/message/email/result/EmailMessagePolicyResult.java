package com.ums.schedule.application.sendrequest.message.email.result;

import com.ums.schedule.application.sendrequest.message.email.command.EmailAttachmentCreateCommand;
import com.ums.schedule.common.code.mapper.EnumMapperValue;
import com.ums.schedule.domain.sendrequest.template.email.code.EmailTemplateSectionEnum;

import java.util.List;
import java.util.Map;

public record EmailMessagePolicyResult(
        EnumMapperValue convertType,
        Map<EmailTemplateSectionEnum, String> fileKeyMap,
        List<EmailAttachmentCreateCommand> attachmentList
) {
    public static EmailMessagePolicyResult of(EnumMapperValue convertType, Map<EmailTemplateSectionEnum, String> templateKeyMap, List<EmailAttachmentCreateCommand> attachments) {
        return new EmailMessagePolicyResult(convertType, templateKeyMap, attachments);
    }
}
