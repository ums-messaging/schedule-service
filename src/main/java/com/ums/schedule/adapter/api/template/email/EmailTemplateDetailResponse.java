package com.ums.schedule.adapter.api.template.email;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public record EmailTemplateDetailResponse(
        String emailContentId,
        String msgTitle,
        String imageDir,
        List<EmailContentResponse> contents
) {

    public Map<EmailTemplateSectionEnum, EmailContentResponse> getHeaderFooter() {
        return this.contents.stream()
                .filter(content -> !content.section().equals(EmailTemplateSectionEnum.BODY))
                .collect(Collectors.toMap(
                        k -> EmailTemplateSectionEnum.valueOf(k.section()), Function.identity(),
                        (o, n) -> n
                ));
    }

    public EmailContentResponse getBody() {
        return this.contents
                .stream()
                .filter(content->content.section().equals(EmailTemplateSectionEnum.BODY.value()))
                .findFirst()
                .orElse(null);
    }

    public List<EmailContentResponse> getAttachmentList() {
        return this.contents
                .stream()
                .filter(content->content.section().equals(EmailTemplateSectionEnum.ATTACHMENT.value()))
                .collect(Collectors.toList());
    }
}
