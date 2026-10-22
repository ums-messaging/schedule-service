package com.ums.schedule.email.application.template.query;

import com.ums.schedule.email.adaptor.in.config.TemplateConfigProvider;
import com.ums.schedule.email.application.template.config.TemplateConfig;
import com.ums.schedule.orchestrator.adaptor.out.storage.AwsS3FileMetadataResponse;
import com.ums.schedule.orchestrator.adaptor.out.storage.AwsS3Repository;
import com.ums.schedule.common.code.email.EmailMessageSection;
import com.ums.schedule.email.application.template.query.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class TemplateFileService {
    private final TemplateConfigProvider provider;
    private final AwsS3Repository fileRepository;

    public EmailTemplateFileResult findTemplate(String customerId, String templateKey) {
        EmailTemplateContent findHeader = findTemplateContent(customerId, EmailMessageSection.HEADER, templateKey);
        EmailTemplateContent findBody = findTemplateContent(customerId, EmailMessageSection.BODY, templateKey);
        EmailTemplateContent findFooter = findTemplateContent(customerId, EmailMessageSection.FOOTER, templateKey);
        EmailTemplateContent findCover = findTemplateContent(customerId, EmailMessageSection.COVER, templateKey);

        String imageDir = generateImageDir(templateKey);

        List<EmailTemplateContent> contents = List.of(findHeader, findBody, findFooter, findCover);
        Map<EmailMessageSection, EmailTemplateContent> templateMap = getTemplateMap(contents);
        return EmailTemplateFileResult.of(templateMap, imageDir);
    }

    private String generateImageDir(String templateKey) {
        TemplateConfig config = provider.load();
        return config.uploadDir() + "/" + templateKey + "/" + config.imageDir();
    }

    private Map<EmailMessageSection, EmailTemplateContent> getTemplateMap(List<EmailTemplateContent> contents) {
        return contents.stream()
                .filter(v -> v != null)
                .collect(Collectors.toMap(
                        v -> v.section(),
                        Function.identity()));
    }

    private EmailTemplateContent findTemplateContent(String customerId, EmailMessageSection section, String templateKey) {
        String fileKey = generateTemplateKey(customerId, templateKey, section);
        log.info("generate.template.file.key = {}", fileKey);
        AwsS3FileMetadataResponse response = fileRepository.getFileMetadata(fileKey);
        if (response != null) {
            return EmailTemplateContent.of(section, response);
        }
        return null;
    }

    private String generateTemplateKey(String customerId, String templateKey, EmailMessageSection section) {
        TemplateConfig config = provider.load();
        return config.uploadDir() + "/" + customerId + "/" + templateKey + "/" + section.value() + ".html";
    }
}
