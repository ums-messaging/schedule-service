package com.ums.schedule.application.ums.email.attachment;


import com.ums.schedule.domain.sendrequest.message.email.EmailSendMessage;
import com.ums.schedule.domain.sendrequest.resource.email.EmailAttachmentJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EmailAttachmentCreateService {
    private final EmailAttachmentJpaRepository repository;

    @Transactional
    public void create(EmailSendMessage message) {

    }
}
