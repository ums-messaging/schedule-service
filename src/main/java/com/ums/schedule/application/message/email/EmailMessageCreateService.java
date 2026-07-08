package com.ums.schedule.application.message.email;

import com.ums.schedule.adapter.api.request.email.EmailSendCreateRequest;
import com.ums.schedule.application.message.email.model.EmailConvertPolicyCommand;
import com.ums.schedule.application.message.email.model.EmailSendMessageCreateCommand;
import com.ums.schedule.application.message.email.result.EmailMessagePolicyResult;
import com.ums.schedule.application.template.email.query.model.EmailTemplateDetailResult;
import com.ums.schedule.application.template.email.query.model.EmailTemplateResult;
import com.ums.schedule.application.template.email.command.EmailTemplateContentCommand;
import com.ums.schedule.application.message.email.resolver.EmailMessageConvertPolicyResolver;
import com.ums.schedule.application.template.email.query.EmailTemplateQueryService;
import com.ums.schedule.application.message.SendMessageFactory;
import com.ums.schedule.application.template.email.EmailTemplateLoader;
import com.ums.schedule.domain.sendrequest.message.email.EmailSendMessage;
import com.ums.schedule.domain.sendrequest.message.email.EmailSendMessageJpaRepository;
import com.ums.schedule.domain.sendrequest.message.SendMessage;
import com.ums.schedule.domain.sendrequest.SendRequest;
import com.ums.schedule.domain.sendrequest.template.email.code.EmailTemplateSectionEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class EmailMessageCreateService {
    private final EmailTemplateQueryService templateService;
    private final EmailMessageConvertPolicyResolver convertPolicy;
    private final EmailSendMessageJpaRepository messageRepository;

    private final SendMessageFactory messageFactory;
    private final EmailTemplateLoader templateLoader;

    public EmailSendMessage create(SendRequest sendRequest, EmailSendCreateRequest request) {
        EmailTemplateResult template = getEmailTemplateResult(sendRequest, request);

        EmailMessagePolicyResult policy = resolveEmailConvertPolicy(request, template.emailTemplate());
        SendMessage sendMessage = messageFactory.createSendMessage(sendRequest, template.template());

        Map<EmailTemplateSectionEnum, EmailTemplateContentCommand> contentMap = loadTemplate(policy.fileKeyMap());
        EmailSendMessageCreateCommand command = EmailSendMessageCreateCommand.of(template.emailTemplate(), contentMap);
        EmailSendMessage message = EmailSendMessage.of(sendMessage, command, policy.attachmentList());

        return messageRepository.save(message);
    }

    private EmailMessagePolicyResult resolveEmailConvertPolicy(EmailSendCreateRequest request, EmailTemplateDetailResult template) {
        EmailConvertPolicyCommand convertPolicyCommand = EmailConvertPolicyCommand.of(request, template);
        EmailMessagePolicyResult policy = convertPolicy.generateConvertPolicy(convertPolicyCommand);
        return policy;
    }

    private EmailTemplateResult getEmailTemplateResult(SendRequest sendRequest, EmailSendCreateRequest request) {
        String customerId = sendRequest.getCustomerRequestKey().getCustomerId();
        EmailTemplateResult template = templateService.findTemplate(request.toQuery(customerId));
        return template;
    }

    private Map<EmailTemplateSectionEnum, EmailTemplateContentCommand> loadTemplate(Map<EmailTemplateSectionEnum, String> templateMap) {
        return templateMap.entrySet()
                .stream()
                .collect(
                        Collectors.toMap(Map.Entry::getKey,
                        entry -> EmailTemplateContentCommand.of(entry.getValue(), readContent(entry.getValue()))));
    }

    private String readContent(String fileKey) {
        String readTemplate = templateLoader.readTemplate(fileKey);
        return Optional.ofNullable(readTemplate)
                .orElse("");
    }
}