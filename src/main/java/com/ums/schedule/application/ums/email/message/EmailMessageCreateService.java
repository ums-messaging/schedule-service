package com.ums.schedule.application.ums.email.message;

import com.ums.schedule.adapter.api.request.email.EmailSendCreateRequest;
import com.ums.schedule.application.ums.email.attachment.EmailAttachmentCreateService;
import com.ums.schedule.application.ums.email.message.provider.EmailMessagePolicyProvider;
import com.ums.schedule.application.ums.email.message.provider.EmailMessageContext;
import com.ums.schedule.application.ums.common.message.SendMessageFactory;
import com.ums.schedule.application.ums.email.template.query.model.EmailTemplateDetailQuery;
import com.ums.schedule.domain.message.email.attachment.EmailAttachment;
import com.ums.schedule.domain.message.email.EmailSendMessage;
import com.ums.schedule.domain.message.email.EmailSendMessageJpaRepository;
import com.ums.schedule.domain.message.SendMessage;
import com.ums.schedule.domain.sendrequest.SendRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
public class EmailMessageCreateService {
    private final EmailMessagePolicyProvider provider;
    private final SendMessageFactory messageFactory;
    private final EmailAttachmentCreateService attachmentService;
    private final EmailSendMessageJpaRepository messageRepository;

    public EmailSendMessage create(String customerId, SendRequest sendRequest, EmailSendCreateRequest request) {
        EmailTemplateDetailQuery query = request.toQuery(customerId);
        EmailMessageContext result = provider.provide(query, request);

        SendMessage sendMessage = messageFactory.createSendMessage(sendRequest, result.template());
        EmailSendMessage message = EmailSendMessage.of(sendMessage, result);
        messageRepository.save(message);

        List<EmailAttachment> attachments =
                Stream.ofNullable(result.attachments())
                        .filter(list -> list.size() > 0)
                                .map(list ->
                                        attachmentService.create(message, result.securityMail(), list))
                                        .findAny()
                        .orElse(List.of());

        return message;
    }
}