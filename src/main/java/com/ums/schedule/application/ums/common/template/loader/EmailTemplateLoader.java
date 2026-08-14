package com.ums.schedule.application.ums.common.template.loader;

import com.ums.schedule.adapter.storage.AwsS3Repository;
import com.ums.schedule.application.ums.common.exception.TemplateLoadFailException;
import com.ums.schedule.application.ums.common.template.loader.model.EmailTemplate;
import com.ums.schedule.application.ums.email.exception.EmailMessageNotFoundException;
import com.ums.schedule.common.code.api.TemplateErrorCode;
import com.ums.schedule.common.code.email.EmailMessageSection;
import com.ums.schedule.domain.message.email.EmailSendMessage;
import com.ums.schedule.domain.message.email.EmailSendMessageJpaRepository;
import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import software.amazon.awssdk.awscore.exception.AwsServiceException;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;


@Component
@RequiredArgsConstructor
public class EmailTemplateLoader implements TemplateLoader {
    private final EmailSendMessageJpaRepository messageRepository;
    private final AwsS3Repository fileRepository;
    private final Configuration configuration;

    public EmailTemplate loadTemplate(EmailSendMessage sendMessage) {
        Map<EmailMessageSection, String> templateMap = sendMessage.mapToTemplateKey();
        Map<EmailMessageSection, Template> toTemplateMap = toTemplateMap(templateMap);

        return EmailTemplate.of(sendMessage, toTemplateMap);

    }

    private Map<EmailMessageSection, Template> toTemplateMap(Map<EmailMessageSection, String> templateMap) {
        return templateMap.entrySet()
                .stream()
                .collect(Collectors.toMap(
                        v -> v.getKey(),
                        v -> loadTemplateMap(v))
                );
    }

    private Template loadTemplateMap(Map.Entry<EmailMessageSection, String> v) {
        try {
            String readTemplate = readTemplate(v.getValue());

            if(!StringUtils.hasText(readTemplate)) {
                throw TemplateLoadFailException.of(v.getValue(), TemplateErrorCode.TEMPLATE_CONTENT_EMPTY);
            }

            return loadTemplate(v.getValue(), readTemplate);
        } catch (IOException e) {
            throw TemplateLoadFailException.of(v.getValue(), e);
        }
    }

    public String readTemplate(String fileKey) {
        try {
            return fileRepository.getFileStringContent(fileKey);
        } catch (AwsServiceException e) {
            throw TemplateLoadFailException.of(fileKey, e);
        }
    }

    private Template loadTemplate(String messageId, String bodyTemplate) throws IOException {
        return new Template(messageId, bodyTemplate, configuration);
    }
}
