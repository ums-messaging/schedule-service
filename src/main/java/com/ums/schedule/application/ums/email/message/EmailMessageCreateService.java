package com.ums.schedule.application.ums.email.message;

import com.ums.schedule.adapter.api.request.email.request.EmailSendCreateRequest;
import com.ums.schedule.application.ums.email.attachment.EmailAttachmentCreateService;
import com.ums.schedule.application.ums.email.message.model.EmailMessageCreateContext;
import com.ums.schedule.application.ums.email.message.provider.EmailMessagePolicyProvider;
import com.ums.schedule.application.ums.email.message.provider.EmailPolicyResult;
import com.ums.schedule.application.ums.common.message.SendMessageFactory;
import com.ums.schedule.application.ums.email.template.query.EmailTemplateQueryService;
import com.ums.schedule.application.ums.email.template.query.model.EmailAttachmentDetailQuery;
import com.ums.schedule.application.ums.email.template.query.model.EmailTemplateContentResult;
import com.ums.schedule.application.ums.email.template.query.model.EmailTemplateDetailQuery;
import com.ums.schedule.application.ums.email.template.query.model.EmailTemplateResult;
import com.ums.schedule.common.code.email.EmailCode;
import com.ums.schedule.common.code.mapper.EnumMapperFactory;
import com.ums.schedule.common.code.mapper.EnumMapperValue;
import com.ums.schedule.domain.message.email.attachment.EmailAttachment;
import com.ums.schedule.domain.message.email.EmailSendMessage;
import com.ums.schedule.domain.message.email.EmailSendMessageJpaRepository;
import com.ums.schedule.domain.message.SendMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
@Transactional
@RequiredArgsConstructor
public class EmailMessageCreateService {
    private final EnumMapperFactory mapperFactory;
    private final EmailMessagePolicyProvider provider;
    private final SendMessageFactory messageFactory;
    private final EmailTemplateQueryService templateService;
    private final EmailAttachmentCreateService attachmentService;
    private final EmailSendMessageJpaRepository messageRepository;

    @Transactional
    public EmailSendMessage create(String customerId, EmailSendCreateRequest request) {
        EmailTemplateResult findTemplate = findTemplate(customerId, request);

        EmailPolicyResult result = provider.provide(request);

        EmailSendMessage message = createEmailSendMessage(findTemplate, result);
        messageRepository.save(message);

        List<EmailTemplateContentResult> attachmentList = findTemplate.emailTemplate().getAttachmentList();

        if(attachmentList.size() > 0) {
            attachmentService.create(message, attachmentList);
        }
        return message;
    }

    private EmailSendMessage createEmailSendMessage(EmailTemplateResult findTemplate, EmailPolicyResult result) {
        SendMessage sendMessage = messageFactory.createSendMessage(findTemplate.template());
        EmailMessageCreateContext context = EmailMessageCreateContext.of(findTemplate, result);
        EmailSendMessage message = EmailSendMessage.of(sendMessage, context);
        return message;
    }

    private EmailTemplateResult findTemplate(String customerId, EmailSendCreateRequest request) {
        EmailTemplateDetailQuery query = toQuery(customerId, request);
        EmailTemplateResult findTemplate = templateService.findTemplate(query);
        return findTemplate;
    }

    private EmailTemplateDetailQuery toQuery(String customerId, EmailSendCreateRequest request) {
        List<EmailAttachmentDetailQuery> attachmentQueries = request.attachmentList().stream()
                .map(l -> {
                    EnumMapperValue attachmentType = mapperFactory.findEnumMapperValue(EmailCode.ATTACHMENT_TYPE, l.type());
                    return l.toQuery(attachmentType);
                })
                .toList();

        return request.toQuery(customerId, attachmentQueries);
    }
}