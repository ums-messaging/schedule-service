package com.ums.schedule.application.ums.email.template.query;

import com.ums.schedule.adapter.storage.AwsS3FileMetadataResponse;
import com.ums.schedule.adapter.storage.AwsS3Repository;
import com.ums.schedule.application.ums.common.exception.TemplateLoadFailException;
import com.ums.schedule.application.ums.email.template.exception.EmailTemplateNotConfiguredException;
import com.ums.schedule.application.ums.email.template.query.model.EmailTemplateContext;
import com.ums.schedule.application.ums.email.template.query.model.*;
import com.ums.schedule.common.code.api.TemplateErrorCode;
import com.ums.schedule.common.exception.file.FileStorageException;
import com.ums.schedule.common.util.FileUtil;
import com.ums.schedule.config.properties.EmailTemplateProperties;
import com.ums.schedule.common.code.email.EmailUploadPrefixType;
import com.ums.schedule.common.code.email.EmailMessageSection;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
public class S3EmailTemplateQueryService implements EmailTemplateQueryService {
    private final EmailTemplateProperties properties;
    private final AwsS3Repository fileRepository;

    @Override
    public EmailTemplateResult findTemplate(EmailTemplateDetailQuery command) {
        Map<EmailUploadPrefixType, String> propertiesMap = toConfiguredMap(command);
        List<EmailTemplateContentResult> templateList = createTemplateList(command.templateKey(), propertiesMap.get(EmailUploadPrefixType.TEMPLATE_PREFIX), command);
        List<EmailTemplateContentResult> attachments = createAttachmentList(command.templateKey(), propertiesMap.get(EmailUploadPrefixType.ATTACHMENT_SUFFIX), command);
        EmailTemplateDetailResult detail = createEmailTemplateDetail(propertiesMap, command, templateList, attachments);
        return EmailTemplateResult.of(detail);
    }

    private Map<EmailUploadPrefixType, String> toConfiguredMap(EmailTemplateDetailQuery command) {
        String templateDir = generateTemplateDir(command);

        Map<EmailUploadPrefixType, String> propertiesMap = new EnumMap<>(EmailUploadPrefixType.class);
        propertiesMap.put(EmailUploadPrefixType.TEMPLATE_PREFIX, templateDir);
        propertiesMap.put(EmailUploadPrefixType.IMAGE_SUFFIX, generateImageDir(templateDir, command.templateKey()));
        propertiesMap.put(EmailUploadPrefixType.ATTACHMENT_SUFFIX, generateAttachmentDir(templateDir, command.templateKey()));

        return propertiesMap;
    }
    private String generateTemplateDir(EmailTemplateDetailQuery command) {
        String prefix = properties.templateKeyPrefix();
        if(!StringUtils.hasText(prefix)) {
            throw EmailTemplateNotConfiguredException.of(EmailUploadPrefixType.TEMPLATE_PREFIX);
        }
        return FileUtil.generateFilePaths(prefix, command.customerId(), command.templateKey());
    }
    private String generateImageDir(String templateDir, String templateKey) {
        String suffix = properties.imageKeySuffix();
        if(!StringUtils.hasText(suffix)) {
            throw EmailTemplateNotConfiguredException.of(EmailUploadPrefixType.IMAGE_SUFFIX);
        }
        return FileUtil.generateFilePaths(templateDir, suffix);
    }
    private String generateAttachmentDir(String templateDir, String templateKey) {
        String suffix = properties.attachmentKeySuffix();
        if(!StringUtils.hasText(suffix)) {
            throw EmailTemplateNotConfiguredException.of(EmailUploadPrefixType.ATTACHMENT_SUFFIX);
        }
        return FileUtil.generateFilePaths(templateDir, suffix);
    }

    private EmailTemplateDetailResult createEmailTemplateDetail(Map<EmailUploadPrefixType, String> propertiesMap,
                                                                EmailTemplateDetailQuery command, List<EmailTemplateContentResult> templateList,
                                                                List<EmailTemplateContentResult> attachments) {
        List<EmailTemplateContentResult> contentList = Stream.concat(templateList.stream(), attachments.stream())
                .toList();
        return EmailTemplateDetailResult.of(propertiesMap, command, contentList);
    }

    private List<EmailTemplateContentResult> createAttachmentList(String templateKey, String attachmentDir, EmailTemplateDetailQuery command) {
        return Optional.ofNullable(command.attachmentKeyList())
                .map(list -> toAttachmentList(templateKey, attachmentDir, list))
                .orElse(Collections.EMPTY_LIST);
    }

    private List<EmailTemplateContentResult> toAttachmentList(String templateKey, String templateDir, List<EmailAttachmentDetailQuery> list) {
        return list
                .stream()
                .map(key -> getEmailContentResult(templateKey, templateDir, key))
                .toList();
    }

    private EmailTemplateContentResult getEmailContentResult(String templateKey, String templateDir, EmailAttachmentDetailQuery query) {
        try {
            EmailTemplateContext context = toAttachmentContext(templateDir, query);

            if(StringUtils.hasText(context.fileKey())) {
                return createEmailContent(context);
            }
            return Optional.ofNullable(context)
                    .filter(ctx -> StringUtils.hasText(ctx.fileKeyTemplate()))
                    .map(EmailTemplateContentResult::of)
                    .orElseThrow();
        } catch (Exception e) {
            throw TemplateLoadFailException.of(templateKey, e);
        }
    }

    private EmailTemplateContext toAttachmentContext(String templateDir, EmailAttachmentDetailQuery query) {
        String fileKey = generateFileName(templateDir, query.fileKey());
        String fileKeyTemplate = generateFileName(templateDir, query.fileKeyTemplate());
        return query.toContext(fileKey, fileKeyTemplate);
    }

    private String generateFileName(String templateDir, String fileKey) {
        if(StringUtils.hasText(fileKey)) {
            return FileUtil.generateFilePaths(templateDir, fileKey);
        }
        return null;
    }

    private List<EmailTemplateContentResult> createTemplateList(String templateKey, String templateDir, EmailTemplateDetailQuery command) {
        List<EmailTemplateContentResult> contents = getEmailSectionList()
                .stream()
                .map(section -> toTemplateContext(section, command, templateDir))
                .map(context -> createEmailContent(context))
                .filter(Objects::nonNull)
                .toList();

        return contents;
    }

    private List<EmailMessageSection> getEmailSectionList() {
        return List.of(
                EmailMessageSection.HEADER,
                EmailMessageSection.BODY,
                EmailMessageSection.FOOTER,
                EmailMessageSection.COVER
        );
    }
    private EmailTemplateContext toTemplateContext(EmailMessageSection section, EmailTemplateDetailQuery command, String templateDir) {
        String fileName = "%s.html".formatted(section.code().toLowerCase());
        String fileKey = FileUtil.generateFilePaths(templateDir, fileName);
        return command.toContext(section, fileKey);
    }


    private EmailTemplateContentResult createEmailContent(EmailTemplateContext context) {
        return Optional.ofNullable(context)
                .filter(ctx -> StringUtils.hasText(ctx.fileKey()))
                .map(ctx -> getFileMetadata(ctx))
                .filter(Objects::nonNull)
                .orElse(null);

    }

    private EmailTemplateContentResult getFileMetadata(EmailTemplateContext context) {
        return Optional.ofNullable(fileRepository.getFileMetadata(context.fileKey()))
                .map(metadata -> context.toContent(metadata))
                .orElse(null);
    }
}
