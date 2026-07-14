package com.ums.schedule.application.ums.email.attachment;


import com.ums.schedule.application.ums.email.attachment.model.AttachmentCreateCommand;
import com.ums.schedule.application.ums.email.convert.ConvertedAttachment;
import com.ums.schedule.application.ums.email.security.SecurityMail;
import com.ums.schedule.domain.message.email.attachment.EmailAttachment;
import com.ums.schedule.domain.message.email.attachment.EmailAttachmentJpaRepository;
import com.ums.schedule.domain.message.email.SecurityMailPolicy;
import com.ums.schedule.domain.message.email.EmailSendMessage;
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
