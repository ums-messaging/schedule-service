package com.ums.schedule.application.sendrequest.message.email.result;

import com.ums.schedule.adapter.api.request.email.EmailSendCreateRequest;
import com.ums.schedule.application.sendrequest.message.email.command.EmailAttachmentCreateCommand;
import com.ums.schedule.domain.sendrequest.template.email.code.EmailTemplateSectionEnum;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public record EmailTemplateDetailResult(
        String emailContentId,
        String msgTitle,
        String imageDir,
        List<EmailContentResult> contents
) {

    public static EmailTemplateDetailResult of(String templateKey, EmailSendCreateRequest request, List<EmailContentResult> contents) {
        List<EmailContentResult> toContents = Stream.concat(contents.stream(),
                request.attachmentList().stream()
                        .map(form -> EmailContentResult.of(form))
        ).toList();
        return new EmailTemplateDetailResult(templateKey, request.title(), templateKey+"/images", toContents);
    }

    public Map<EmailTemplateSectionEnum, EmailContentResult> getHeaderFooter() {
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

    public EmailContentResult getBody() {
        if(this.contents == null) {
            return null;
        }
        return this.contents
                .stream()
                .filter(content-> content.section().toUpperCase().equals(EmailTemplateSectionEnum.BODY.code()))
                .findFirst()
                .orElse(null);
    }

    public List<EmailContentResult> getAttachmentList() {
        return this.contents
                .stream()
                .filter(content->content.section().equals(EmailTemplateSectionEnum.ATTACHMENT.value()))
                .collect(Collectors.toList());
    }

    public List<EmailAttachmentCreateCommand> toAttachmentCommandList() {
        return getAttachmentList().stream()
                .map(attachment -> EmailAttachmentCreateCommand.of(attachment))
                .toList();
    }
}
