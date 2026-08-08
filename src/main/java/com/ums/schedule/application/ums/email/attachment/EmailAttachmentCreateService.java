package com.ums.schedule.application.ums.email.attachment;


import com.ums.schedule.application.ums.email.attachment.model.AttachmentContext;
import com.ums.schedule.application.ums.email.template.query.model.EmailTemplateContentResult;
import com.ums.schedule.domain.message.email.attachment.EmailAttachment;
import com.ums.schedule.domain.message.email.attachment.EmailAttachmentJpaRepository;
import com.ums.schedule.domain.message.email.EmailSendMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmailAttachmentCreateService {
    private final EmailAttachmentJpaRepository repository;

    @Transactional
    public List<EmailAttachment> create(EmailSendMessage sendMessage, List<EmailTemplateContentResult> templates) {
        List<EmailAttachment> attachmentList = templates.stream()
                .map(t -> AttachmentContext.of(t))
                .map(ctx -> EmailAttachment.of(sendMessage, ctx))
                .collect(Collectors.toList());

        repository.saveAll(attachmentList);

        return attachmentList;
    }
}
