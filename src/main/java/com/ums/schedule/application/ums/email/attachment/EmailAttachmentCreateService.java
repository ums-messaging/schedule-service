package com.ums.schedule.application.ums.email.attachment;


import com.ums.schedule.application.ums.email.attachment.model.AttachmentCreateCommand;
import com.ums.schedule.application.ums.email.attachment.model.AttachmentListCreateCommand;
import com.ums.schedule.application.ums.email.convert.EmailConvertPolicy;
import com.ums.schedule.application.ums.email.attachment.model.AttachmentContext;
import com.ums.schedule.application.ums.email.template.query.model.EmailTemplateContentResult;
import com.ums.schedule.domain.message.email.EmailAttachment;
import com.ums.schedule.domain.message.email.EmailAttachmentJpaRepository;
import com.ums.schedule.domain.message.exception.EmailSendMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class EmailAttachmentCreateService {
    private final EmailAttachmentJpaRepository repository;

    @Transactional
    public List<EmailAttachment> create(AttachmentListCreateCommand command) {
        List<AttachmentCreateCommand> attachments = combineAttachments(command.sendMessage(), command.convertPolicy(), command.attachments());
        List<EmailAttachment> attachmentList = attachments.stream()
                .map(EmailAttachment::of)
                .toList();

        repository.saveAll(attachmentList);

        return attachmentList;
    }

    private List<AttachmentCreateCommand> combineAttachments(EmailSendMessage sendMessage, EmailConvertPolicy convertPolicy, List<AttachmentContext> attachmentList) {
        return Stream.concat(
                    Stream.ofNullable(convertPolicy.convertedAttachment())
                        .map(attachment -> AttachmentCreateCommand.of(sendMessage, convertPolicy.securityMailPolicy(), attachment))
                        ,
                Stream.ofNullable(attachmentList)
                        .flatMap(List::stream)
                        .map(context -> AttachmentCreateCommand.of(sendMessage, context))
        ).toList();
    }
}
