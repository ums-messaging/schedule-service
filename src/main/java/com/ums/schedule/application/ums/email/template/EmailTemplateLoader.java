package com.ums.schedule.application.ums.email.template;

import com.ums.schedule.adapter.storage.AwsS3Repository;
import com.ums.schedule.application.exception.EmailMessageNotFoundException;
import com.ums.schedule.common.util.UuidUtil;
import com.ums.schedule.domain.sendrequest.message.email.EmailSendMessage;
import com.ums.schedule.domain.sendrequest.message.email.EmailSendMessageJpaRepository;
import com.ums.schedule.domain.sendrequest.message.email.exception.MessageParseException;
import com.ums.schedule.domain.sendrequest.target.code.TargetColumnEnum;
import com.ums.schedule.domain.sendrequest.template.email.EmailTemplate;
import com.ums.schedule.application.exception.TemplateLoadFailedException;
import com.ums.schedule.domain.sendrequest.template.exception.TemplatePolicyViolationException;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.Map;
import java.util.UUID;


@Component
@RequiredArgsConstructor
public class EmailTemplateLoader {
    private final EmailSendMessageJpaRepository messageRepository;
    private final AwsS3Repository fileRepository;
    private final Configuration configuration;

    public String loadAndCompileTemplate(String fileKey, Map<String, Object> messageVariable) {
        try (Writer writer = new StringWriter()) {
            return loadAndCompileTemplate(fileKey, messageVariable, writer);
        } catch (IOException e) {
            throw TemplateLoadFailedException.of(e);
        }
    }

    private String loadAndCompileTemplate(String fileKey, Map<String, Object> messageVariable, Writer writer) throws IOException {
        String fileContent = readTemplate(fileKey);
        try {
            Template template = loadTemplate(fileKey, fileContent);
            template.process(messageVariable, writer);
        } catch (TemplateException e) {
            TargetColumnEnum targetKey = TargetColumnEnum.TARGET_KEY;
            throw MessageParseException.of(fileKey, String.valueOf(messageVariable.get(targetKey.value().toLowerCase())));
        }
        return fileContent;
    }

    public String readTemplate(String fileKey) {
        return fileRepository.getFileStringContent(fileKey);
    }

    public EmailTemplate loadTemplate(String messageId) {
        try {
            UUID uuid = UuidUtil.decode(messageId);
            EmailSendMessage findMessage = messageRepository.findById(uuid)
                    .orElseThrow(() -> EmailMessageNotFoundException.of(messageId));
            Template html = loadTemplate(messageId, findMessage.getBodyTemplate());
            return EmailTemplate.of(findMessage.getSubject(), html, findMessage.getAttachmentList());
        } catch (IOException e) {
            throw TemplateLoadFailedException.of(e);
        }
    }

    private Template loadTemplate(String messageId, String bodyTemplate) throws IOException {
        return new Template(messageId, bodyTemplate, configuration);
    }
}
