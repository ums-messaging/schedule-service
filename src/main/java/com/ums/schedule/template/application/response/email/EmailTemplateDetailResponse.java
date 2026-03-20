package com.ums.schedule.template.application.response.email;

import com.ums.schedule.attachment.domain.Attachment;
import com.ums.schedule.message.domain.email.EmailSendMessage;
import com.ums.schedule.template.domain.code.EmailTemplateSectionEnum;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.ums.schedule.template.domain.code.EmailTemplateSectionEnum.*;

public record EmailTemplateDetailResponse(
        String emailContentId,
        String msgTitle,
        String imageDir,
        List<EmailContentResponse> contents
) {

    public Map<String, EmailContentResponse> getHeaderFooter() {
        return this.contents.stream()
                .filter(content -> content.section().equals(HEADER.value()) || content.section().equals(FOOTER.value()))
                .collect(Collectors.toMap(
                        k -> k.section(), Function.identity(),
                        (o, n) -> n
                ));
    }

    public EmailContentResponse getBody() {
        return this.contents
                .stream()
                .filter(content->content.section().equals(BODY.value()))
                .findFirst()
                .orElse(null);
    }

    public List<EmailContentResponse> getAttachmentList() {
        return this.contents
                .stream()
                .filter(content->content.section().equals(ATTACHMENT.value()))
                .collect(Collectors.toList());
    }
}
