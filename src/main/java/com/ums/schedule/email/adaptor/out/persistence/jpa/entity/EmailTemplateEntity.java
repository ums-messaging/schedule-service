package com.ums.schedule.email.adaptor.out.persistence.jpa.entity;

import com.github.f4b6a3.uuid.UuidCreator;
import com.ums.schedule.email.domain.common.FileMetadata;
import com.ums.schedule.email.domain.template.EmailTemplate;
import com.ums.schedule.orchestrator.domain.event.converter.UuidBinaryConverter;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.domain.Persistable;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Entity
@Table(name = "email_template",
        uniqueConstraints = {
                @UniqueConstraint(
                        name="uq_customer_template_key",
                        columnNames = {"customer_id", "template_key"}
                )
        }
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EmailTemplateEntity extends BaseEntity {

    @Id
    @UuidGenerator
    @GeneratedValue
    @Convert(converter = UuidBinaryConverter.class)
    private UUID id;

    @Column(name = "customer_id", nullable = false)
    private String customerId;

    @Column(name = "template_key", nullable = false)
    private String templateKey;

    @Column(name = "cover_key")
    private String coverKey;
    @Column(name = "cover_size")
    private Long coverSize;

    @Column(name = "header_key")
    private String headerKey;
    @Column(name = "header_size")
    private Long headerSize;
    @Column(name = "body_key", nullable = false)
    private String bodyKey;
    @Column(name = "body_size", nullable = false)
    private Long bodySize;
    @Column(name = "footer_key")
    private String footerKey;
    @Column(name = "footer_size")
    private Long footerSize;

    private String imageDir;

    public static EmailTemplateEntity fromDomain(EmailTemplate domain) {
        EmailTemplateEntity entity = new EmailTemplateEntity();
        entity.assignId(domain.getId());
        entity.assignUniqueKey(domain.getCustomerId(), domain.getTemplateKey());
        entity.assignCover(domain.getCover());
        entity.assignHeader(domain.getHeader());
        entity.assignBody(domain.getBody());
        entity.assignFooter(domain.getFooter());
        entity.assignImageDir(domain.getImageDir());
        entity.assignCreatedAt(domain.getCreatedAt());
        return entity;
    }

    private void assignCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    private void assignUniqueKey(String customerId, String templateKey) {
        this.customerId = customerId;
        this.templateKey = templateKey;
    }

    private void assignId(String id) {
        if(id != null) {
            this.id = UuidCreator.fromString(id);
        }
    }

    private void assignCover(FileMetadata metadata) {
        this.coverKey = metadata.fileKey();
        this.coverSize = metadata.fileSize();
    }
    private void assignHeader(FileMetadata metadata) {
        this.headerKey = metadata.fileKey();
        this.headerSize = metadata.fileSize();
    }
    private void assignBody(FileMetadata metadata) {
        this.bodyKey = metadata.fileKey();
        this.bodySize = metadata.fileSize();
    }
    private void assignFooter(FileMetadata metadata) {
        this.footerKey = metadata.fileKey();
        this.footerSize = metadata.fileSize();
    }
    private void assignImageDir(String imageDir) {
        this.imageDir = imageDir;
    }

    public EmailTemplate toDomain() {
        return EmailTemplate.fromEntity(this);
    }
}
