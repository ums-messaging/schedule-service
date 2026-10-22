package com.ums.schedule.email.application.message;

import com.ums.schedule.email.adaptor.in.rest.message.request.MessageCreateRequest;
import com.ums.schedule.email.application.attachment.EmailAttachmentCreateService;
import com.ums.schedule.email.application.message.command.EmailSendMessageCreateCommand;
import com.ums.schedule.email.application.message.provider.EmailMessagePolicyProvider;
import com.ums.schedule.email.application.message.provider.EmailPolicyResult;

import com.ums.schedule.email.application.message.command.SendMessageCreateSummary;
import com.ums.schedule.email.domain.attachment.EmailAttachment;
import com.ums.schedule.email.domain.message.EmailSendMessage;
import com.ums.schedule.email.domain.message.EmailSendMessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class EmailMessageCreateService {
    private final EmailMessagePolicyProvider provider;
    private final EmailAttachmentCreateService attachmentService;
    private final EmailSendMessageRepository messageRepository;

    @Transactional
    public SendMessageCreateSummary create(String customerId, MessageCreateRequest request) {
        EmailPolicyResult result = provider.provide(request);
        EmailSendMessageCreateCommand command = EmailSendMessageCreateCommand.of(customerId, request, result);
        log.info("result ={}, command = {}", request, command);
        EmailSendMessage message = EmailSendMessage.of(command);
        EmailSendMessage saved = messageRepository.save(message);
        List<EmailAttachment> attachments = Optional.ofNullable(request.attachmentList())
                .filter(l -> !l.isEmpty())
                .map(l -> attachmentService.create(saved.getId(), l))
                .orElseGet(() -> new ArrayList<>());
        if(request.attachmentList() != null) {
            attachmentService.create(saved.getId(), request.attachmentList());
        }
        return SendMessageCreateSummary.of(saved, attachments);
    }
}