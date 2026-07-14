package com.ums.schedule.application.ums.email.template.query.model;

import com.ums.schedule.common.code.email.EmailTemplatePathTypeEnum;
import com.ums.schedule.common.code.email.EmailTemplateSectionEnum;

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

    public static EmailTemplateDetailResult of(Map<EmailTemplatePathTypeEnum, String> propertiesMap, EmailTemplateDetailQuery command, List<EmailTemplateContentResult> templateList) {
        return new EmailTemplateDetailResult(
                command.templateKey(),
                command.title(),
                propertiesMap.get(EmailTemplatePathTypeEnum.IMAGE_SUFFIX),
                templateList
        );
    }

    public Map<EmailTemplateSectionEnum, EmailTemplateContentResult> getHeaderFooter() {
        if(this.contents == null) {
            return Map.of();
        }
        return this.contents.stream()
                .filter(content -> !content.section().equals(EmailTemplateSectionEnum.BODY.code().toLowerCase()))
                .collect(Collectors.toMap(
                        k -> EmailTemplateSectionEnum.valueOf(k.section().toUpperCase()), Function.identity(),
                        (o, n) -> n
                ));
    }

    public EmailTemplateContentResult getBody() {
        if(this.contents == null) {
            return null;
        }
        return this.contents
                .stream()
                .filter(content-> content.section().toUpperCase().equals(EmailTemplateSectionEnum.BODY.code()))
                .findFirst()
                .orElse(null);
    }

    public List<EmailTemplateContentResult> getAttachmentList() {
        return this.contents
                .stream()
                .filter(content->content.section().equals(EmailTemplateSectionEnum.ATTACHMENT.value()))
                .collect(Collectors.toList());
    }
}
