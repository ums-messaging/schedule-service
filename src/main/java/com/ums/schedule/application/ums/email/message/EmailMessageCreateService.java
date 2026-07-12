package com.ums.schedule.application.ums.email.message;

import com.ums.schedule.adapter.api.request.email.EmailSecurityPolicyRequest;
import com.ums.schedule.adapter.api.request.email.EmailSendCreateRequest;
import com.ums.schedule.application.message.email.model.EmailSendMessageCreateCommand;
import com.ums.schedule.application.ums.email.attachment.EmailAttachmentCreateService;
import com.ums.schedule.application.ums.email.attachment.model.AttachmentListCreateCommand;
import com.ums.schedule.application.ums.email.convert.resolver.model.EmailConvertResolveCommand;
import com.ums.schedule.application.ums.email.convert.EmailConvertPolicy;
import com.ums.schedule.application.ums.email.security.SecurityMail;
import com.ums.schedule.application.ums.email.security.SecurityMailAssembler;
import com.ums.schedule.application.ums.email.template.query.EmailTemplateQueryService;
import com.ums.schedule.application.ums.email.template.query.model.EmailTemplateResult;
import com.ums.schedule.application.ums.email.convert.resolver.EmailConvertResolver;
import com.ums.schedule.application.ums.common.message.SendMessageFactory;
import com.ums.schedule.domain.message.email.EmailAttachment;
import com.ums.schedule.domain.message.exception.EmailSendMessage;
import com.ums.schedule.domain.message.exception.EmailSendMessageJpaRepository;
import com.ums.schedule.domain.sendrequest.message.SendMessage;
import com.ums.schedule.domain.sendrequest.SendRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class EmailMessageCreateService {
    private final SecurityMailAssembler securityAssembler;
    private final EmailTemplateQueryService templateService;
    private final EmailConvertResolver resolver;
    private final EmailAttachmentCreateService attachmentService;
    private final EmailSendMessageJpaRepository messageRepository;
    private final SendMessageFactory messageFactory;

    public EmailSendMessage create(SendRequest sendRequest, EmailSendCreateRequest request) {
        EmailTemplateResult template = getEmailTemplateResult(sendRequest, request);
        SendMessage sendMessage = messageFactory.createSendMessage(sendRequest, template.template());

        SecurityMail securityMail = createSecurityMail(request);
        EmailConvertPolicy policy = createConvertPolicy(request, template, securityMail);
        EmailSendMessageCreateCommand command = EmailSendMessageCreateCommand.of(sendMessage, template.emailTemplate(), policy);

        EmailSendMessage message = EmailSendMessage.of(command);
        messageRepository.save(message);

        AttachmentListCreateCommand attachmentListCommand = AttachmentListCreateCommand.of(message, policy, template.emailTemplate().getAttachmentList());
        List<EmailAttachment> attachments = attachmentService.create(attachmentListCommand);

        return message;
    }

    private EmailConvertPolicy createConvertPolicy(EmailSendCreateRequest request, EmailTemplateResult template, SecurityMail securityMail) {
        EmailConvertResolveCommand command = EmailConvertResolveCommand.of(request, template.emailTemplate());
        return resolver.resolve(command, securityMail);
    }

    private SecurityMail createSecurityMail(EmailSendCreateRequest request) {
        return Optional.ofNullable(request.securityPolicy())
                .map(EmailSecurityPolicyRequest::toCommand)
                .map(command -> securityAssembler.assemble(command))
                .orElseGet(null);
    }

    private EmailTemplateResult getEmailTemplateResult(SendRequest sendRequest, EmailSendCreateRequest request) {
        String customerId = sendRequest.getCustomerRequestKey().getCustomerId();
        EmailTemplateResult template = templateService.findTemplate(request.toQuery(customerId));
        return template;
    }
}