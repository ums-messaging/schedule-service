package com.ums.schedule.email.domain.template;

import com.github.f4b6a3.uuid.UuidCreator;
import com.ums.schedule.email.adaptor.out.persistence.jpa.entity.EmailTemplateEntity;
import com.ums.schedule.email.application.template.command.EmailTemplateCreateCommand;
import com.ums.schedule.email.domain.common.FileMetadata;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Optional;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EmailTemplate {
    private String id;

    private String customerId;
    private String templateKey;

    private FileMetadata header;
    private FileMetadata body;
    private FileMetadata footer;
    private FileMetadata cover;

    private String imageDir;
    private LocalDateTime createdAt;


    public static EmailTemplate of(EmailTemplateCreateCommand command) {
        EmailTemplate template = new EmailTemplate(command.customerId(), command.templateKey());
        template.assignCover(command.coverKey(), command.coverSize());
        template.assignHeader(command.headerKey(), command.headerSize());
        template.assignBody(command.bodyKey(), command.bodySize());
        template.assignFooter(command.footerKey(), command.footerSize());
        template.assignImageDir(command.imageDir(), command.defaultImageDir());
        return template;
    }

    protected EmailTemplate(String customerId, String templateKey) {
        this.customerId = customerId;
        this.templateKey = templateKey;
    }

    private void assignImageDir(String imageDir, String defaultImageDir) {
        this.imageDir = StringUtils.hasText(imageDir) ? imageDir : defaultImageDir;
    }

    private void assignCover(String coverKey, Long coverSize) {
        this.cover = assignTemplateData(coverKey, coverSize);
    }

    private void assignHeader(String headerKey, Long headerSize) {
        this.header = assignTemplateData(headerKey, headerSize);
    }

    private void assignFooter(String footerKey, Long footerSize) {
        this.footer = assignTemplateData(footerKey, footerSize);
    }

    private void assignBody(String bodyKey, Long bodySize) {
        this.body = Optional.ofNullable(
                        assignTemplateData(bodyKey, bodySize)
                    ).orElseThrow(() -> new IllegalArgumentException("바디 템플릿이 존재하지 않습니다."));
    }

    private FileMetadata assignTemplateData(String fileKey, Long fileSize) {
        if (fileSize != null) {
            return FileMetadata.of(fileKey, fileSize);
        }
        return null;
    }

    public static EmailTemplate fromEntity(EmailTemplateEntity entity) {
        return new EmailTemplate(entity);
    }
    public EmailTemplateEntity toEntity() {
        return EmailTemplateEntity.fromDomain(this);
    }
    private EmailTemplate(EmailTemplateEntity entity) {
        this.id = UuidCreator.toString(entity.getId());
        this.customerId = entity.getCustomerId();
        this.templateKey = entity.getTemplateKey();
        this.cover = FileMetadata.of(entity.getCoverKey(), entity.getCoverSize());
        this.header = FileMetadata.of(entity.getHeaderKey(), entity.getHeaderSize());
        this.body = FileMetadata.of(entity.getBodyKey(), entity.getBodySize());
        this.footer = FileMetadata.of(entity.getFooterKey(), entity.getFooterSize());
        this.imageDir = entity.getImageDir();
        this.createdAt = entity.getCreatedAt();
    }

    public String getHeaderKey() {
        return Optional.ofNullable(header)
                .map(FileMetadata::fileKey)
                .orElse(null);
    }
    public String getBodyKey() {
        return Optional.ofNullable(body)
                .map(FileMetadata::fileKey)
                .orElse(null);
    }
    public String getFooterKey() {
        return Optional.ofNullable(footer)
                .map(FileMetadata::fileKey)
                .orElse(null);
    }
    public String getCoverKey() {
        return Optional.ofNullable(cover)
                .map(FileMetadata::fileKey)
                .orElse(null);
    }

    public Long getHeaderSize() {
        return Optional.ofNullable(header)
                .map(FileMetadata::fileSize)
                .orElse(null);
    }
    public Long getBodySize() {
        return Optional.ofNullable(body)
                .map(FileMetadata::fileSize)
                .orElse(null);
    }
    public Long getFooterSize() {
        return Optional.ofNullable(footer)
                .map(FileMetadata::fileSize)
                .orElse(null);
    }
    public Long getCoverSize() {
        return Optional.ofNullable(cover)
                .map(FileMetadata::fileSize)
                .orElse(null);
    }}
