package com.ums.schedule.application.ums.email.template.query.model;

import com.ums.schedule.common.code.email.EmailUploadPrefixType;
import com.ums.schedule.common.code.email.EmailMessageSection;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public record EmailTemplateDetailResult(
        String emailContentId,
        String msgTitle,
        String imageDir,
        List<EmailTemplateContentResult> contents
) {

    public static EmailTemplateDetailResult of(Map<EmailUploadPrefixType, String> propertiesMap, EmailTemplateDetailQuery command, List<EmailTemplateContentResult> templateList) {
        return new EmailTemplateDetailResult(
                command.templateKey(),
                command.title(),
                propertiesMap.get(EmailUploadPrefixType.IMAGE_SUFFIX),
                templateList
        );
    }

    public Map<EmailMessageSection, EmailTemplateContentResult> getHeaderFooter() {
        if(this.contents == null) {
            return Map.of();
        }
        return this.contents.stream()
                .filter(content -> !content.section().equals(EmailMessageSection.BODY.code().toLowerCase()))
                .collect(Collectors.toMap(
                        k -> EmailMessageSection.valueOf(k.section().toUpperCase()), Function.identity(),
                        (o, n) -> n
                ));
    }

    public EmailTemplateContentResult getBody() {
        if(this.contents == null) {
            return null;
        }
        return this.contents
                .stream()
                .filter(content-> content.section().toUpperCase().equals(EmailMessageSection.BODY.code()))
                .findFirst()
                .orElse(null);
    }

    public List<EmailTemplateContentResult> getAttachmentList() {
        return this.contents
                .stream()
                .filter(content->content.section().equals(EmailMessageSection.ATTACHMENT.value()))
                .collect(Collectors.toList());
    }
}
