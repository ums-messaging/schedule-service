package com.ums.schedule.email.application.attachment;


import com.ums.schedule.email.adaptor.in.rest.message.request.AttachmentRequest;
import com.ums.schedule.email.application.attachment.file.AttachmentFileService;
import com.ums.schedule.email.application.attachment.model.AttachmentFindResult;
import com.ums.schedule.email.application.attachment.model.AttachmentCreateCommand;
import com.ums.schedule.email.domain.attachment.EmailAttachment;
import com.ums.schedule.email.domain.attachment.EmailAttachmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmailAttachmentCreateService {
    private final AttachmentFileService fileService;
    private final EmailAttachmentRepository repository;

    @Transactional
    public List<EmailAttachment> create(UUID messageId, List<AttachmentRequest> requests) {
        List<EmailAttachment> attachments = requests.stream()
                .map(request -> {
                    AttachmentFindResult result = fileService.getAttachmentResult(request.type(), request.fileKey());
                    return AttachmentCreateCommand.of(request, result);
                })
                .map(EmailAttachment::of)
                .toList();
        return repository.saveAll(messageId, attachments);
    }
}
