package com.ums.schedule.application.ums.email.template.query;

import com.ums.schedule.adapter.storage.AwsS3FileMetadataResponse;
import com.ums.schedule.adapter.storage.AwsS3Repository;
import com.ums.schedule.application.ums.common.exception.TemplateLoadFailException;
import com.ums.schedule.application.ums.email.template.exception.EmailTemplateNotConfiguredException;
import com.ums.schedule.application.ums.email.template.query.model.EmailTemplateContext;
import com.ums.schedule.application.ums.email.template.query.model.*;
import com.ums.schedule.common.code.email.AttachmentType;
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
    public EmailTemplateResult findTemplate(EmailTemplateDetailQuery query) {
        Map<EmailUploadPrefixType, String> propertiesMap = toConfiguredMap(query);
        List<EmailTemplateContentResult> templateList = createTemplateList(propertiesMap.get(EmailUploadPrefixType.TEMPLATE_PREFIX), query);
        List<EmailTemplateContentResult> attachments = createAttachmentList(query.templateKey(), propertiesMap.get(EmailUploadPrefixType.ATTACHMENT_SUFFIX), query.attachmentQueries());
        EmailTemplateDetailResult detail = createEmailTemplateDetail(propertiesMap, query, templateList, attachments);
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
        String prefix = properties.getTemplateKeyPrefix();
        if(!StringUtils.hasText(prefix)) {
            throw EmailTemplateNotConfiguredException.of(EmailUploadPrefixType.TEMPLATE_PREFIX);
        }
        return FileUtil.generateFilePaths(prefix, command.customerId(), command.templateKey());
    }

    private String generateImageDir(String templateDir, String templateKey) {
        String suffix = properties.getImageKeySuffix();
        if(!StringUtils.hasText(suffix)) {
            throw EmailTemplateNotConfiguredException.of(EmailUploadPrefixType.IMAGE_SUFFIX);
        }
        return FileUtil.generateFilePaths(templateDir, suffix);
    }
    private String generateAttachmentDir(String templateDir, String templateKey) {
        String suffix = properties.getAttachmentKeySuffix();
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

    private List<EmailTemplateContentResult> createAttachmentList(String templateKey, String attachmentDir, List<EmailAttachmentDetailQuery> queries) {
        return Optional.ofNullable(queries)
                .map(list -> toAttachmentList(templateKey, attachmentDir, list))
                .orElse(Collections.EMPTY_LIST);
    }

    private List<EmailTemplateContentResult> toAttachmentList(String templateKey, String templateDir, List<EmailAttachmentDetailQuery> list) {
        return list
                .stream()
                .map(key -> attachmentToContent(templateKey, templateDir, key))
                .toList();
    }

    private EmailTemplateContentResult attachmentToContent(String templateKey, String templateDir, EmailAttachmentDetailQuery query) {
        String fileKey = generateFileName(templateDir, query.fileKey());

        if(query.type() == AttachmentType.DIRECT) {
            try {
                AwsS3FileMetadataResponse fileMetadata = fileRepository.getFileMetadata(fileKey);
                return EmailTemplateContentResult.of(query, fileKey, fileMetadata);
            } catch (FileStorageException e) {
                throw TemplateLoadFailException.of(templateKey, e);
            }

        }
        return EmailTemplateContentResult.of(query, fileKey);
    }

    private String generateFileName(String templateDir, String fileKey) {
        if(StringUtils.hasText(fileKey)) {
            return FileUtil.generateFilePaths(templateDir, fileKey);
        }
        return null;
    }

    private List<EmailTemplateContentResult> createTemplateList(String templateDir, EmailTemplateDetailQuery command) {
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
