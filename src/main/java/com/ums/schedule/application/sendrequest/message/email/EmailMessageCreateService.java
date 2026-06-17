package com.ums.schedule.application.sendrequest.message.email;

import com.ums.schedule.adapter.api.request.email.EmailSendCreateRequest;
import com.ums.schedule.application.sendrequest.message.email.command.EmailConvertPolicyCommand;
import com.ums.schedule.application.sendrequest.message.email.command.EmailSendMessageCreateCommand;
import com.ums.schedule.application.sendrequest.template.email.command.EmailTemplateContentCommand;
import com.ums.schedule.application.sendrequest.message.email.policy.EmailMessageConvertTypePolicy;
import com.ums.schedule.application.sendrequest.message.email.result.EmailMessagePolicyResult;
import com.ums.schedule.application.sendrequest.message.email.result.EmailTemplateDetailResult;
import com.ums.schedule.application.sendrequest.message.email.result.EmailTemplateResult;
import com.ums.schedule.application.sendrequest.template.email.EmailTemplateService;
import com.ums.schedule.application.sendrequest.message.SendMessageFactory;
import com.ums.schedule.application.sendrequest.template.loader.EmailTemplateLoader;
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
    private final EmailTemplateService templateService;
    private final EmailMessageConvertTypePolicy convertPolicy;
    private final EmailSendMessageJpaRepository messageRepository;

    private final SendMessageFactory messageFactory;
    private final EmailTemplateLoader templateLoader;

    public EmailSendMessage create(SendRequest sendRequest, EmailSendCreateRequest request) {
        EmailTemplateResult template = templateService.findTemplate(sendRequest.getTemplateKey(), request);
        EmailTemplateDetailResult emailTemplate = template.emailTemplate();

        EmailConvertPolicyCommand convertPolicyCommand = EmailConvertPolicyCommand.of(request, emailTemplate);
        EmailMessagePolicyResult policy = convertPolicy.generateConvertPolicy(convertPolicyCommand);
        SendMessage sendMessage = messageFactory.createSendMessage(sendRequest, template.template());

        Map<EmailTemplateSectionEnum, EmailTemplateContentCommand> contentMap = loadTemplate(policy.fileKeyMap());
        EmailSendMessageCreateCommand command = EmailSendMessageCreateCommand.of(emailTemplate, contentMap);
        EmailSendMessage message = EmailSendMessage.of(sendMessage, command, policy.attachmentList());

        return messageRepository.save(message);
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