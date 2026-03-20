package com.ums.schedule.attachment.application.service;

import com.ums.schedule.attachment.domain.Attachment;
import com.ums.schedule.attachment.domain.AttachmentPolicy;
import com.ums.schedule.attachment.exception.AttachmentPolicyRequiredException;
import com.ums.schedule.message.application.command.EmailMessageCommand;
import com.ums.schedule.template.application.response.email.EmailContentResponse;
import com.ums.schedule.template.domain.email.EmailContent;
import com.ums.schedule.template.domain.email.EmailTemplate;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public interface AttachmentHandler {
    Attachment handle(EmailMessageCommand command, EmailContentResponse body);

    default List<Attachment> handler(EmailMessageCommand command, EmailContentResponse body, List<EmailContentResponse> attachmentList) {
        Attachment fromBody = handle(command, body);
        return Stream.concat(
                Optional.ofNullable(fromBody)
                    .map(Stream::of)
                    .orElseGet(Stream::empty),
                attachmentList.stream()
                        .map(attachment ->
                                Optional.ofNullable(handle(command, attachment))
                                        .orElseThrow(AttachmentPolicyRequiredException::ofDownloadOrAttachmentName)
                        )
        ).toList();
    }
}
