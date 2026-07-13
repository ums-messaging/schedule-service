package com.ums.schedule.application.ums.email.attachment;


import com.ums.schedule.application.ums.email.attachment.model.AttachmentCreateCommand;
import com.ums.schedule.application.ums.email.attachment.model.AttachmentCreateResult;
import com.ums.schedule.application.ums.email.attachment.model.AttachmentListCreateCommand;
import com.ums.schedule.application.ums.email.convert.ConvertedAttachment;
import com.ums.schedule.application.ums.email.convert.EmailConvertPolicy;
import com.ums.schedule.application.ums.email.attachment.model.AttachmentContext;
import com.ums.schedule.application.ums.email.security.SecurityMail;
import com.ums.schedule.application.ums.email.template.query.model.EmailTemplateContentResult;
import com.ums.schedule.domain.message.email.EmailAttachment;
import com.ums.schedule.domain.message.email.EmailAttachmentJpaRepository;
import com.ums.schedule.domain.message.email.SecurityMailPolicy;
import com.ums.schedule.domain.message.exception.EmailSendMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmailAttachmentCreateService {
    private final EmailAttachmentJpaRepository repository;

    @Transactional
    public List<EmailAttachment> create(EmailSendMessage sendMessage, SecurityMail securityMail, List<ConvertedAttachment> commands) {
        SecurityMailPolicy securityPolicy = Optional.ofNullable(securityMail)
                .map(SecurityMailPolicy::of)
                .orElse(null);
        List<EmailAttachment> attachmentList = commands.stream()
                .map(command -> AttachmentCreateCommand.of(sendMessage, securityPolicy, command))
                .map(EmailAttachment::of)
                .toList();

        repository.saveAll(attachmentList);

        return attachmentList;
    }
}
