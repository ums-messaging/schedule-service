package com.ums.schedule.email.adaptor.out.persistence.jpa.entity;

import com.github.f4b6a3.uuid.UuidCreator;
import com.ums.schedule.common.code.email.ConvertType;
import com.ums.schedule.common.code.email.EmailType;

import com.ums.schedule.common.code.email.security.EncryptionTypeEnum;
import com.ums.schedule.common.code.email.security.PasswordHashEnum;
import com.ums.schedule.common.code.email.security.PasswordType;
import com.ums.schedule.common.code.email.security.PermissionMaskEnum;
import com.ums.schedule.email.domain.message.ConvertMessage;
import com.ums.schedule.email.domain.message.EmailSendMessage;
import com.ums.schedule.email.domain.common.MultiPartFile;
import com.ums.schedule.email.domain.message.security.SecurityMail;
import com.ums.schedule.orchestrator.domain.event.converter.UuidBinaryConverter;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.util.*;

@Table(name = "email_send_message")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class EmailSendMessageEntity extends BaseEntity {
    @Id
    @UuidGenerator
    @GeneratedValue
    @Convert(converter = UuidBinaryConverter.class)
    private UUID id;
    private String type;

    private String title;
    private String description;

    private String encryptionType;
    private String passwdType;
    private String passwdHash;
    private String permissionMask;

    private String subject;

    private String convertType;
    private String fileKeyTemplate;
    private String attachmentName;
    private String downloadName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "template_id")
    private EmailTemplateEntity template;

    @OneToMany(mappedBy = "message", fetch = FetchType.LAZY)
    private List<EmailAttachmentEntity> attachmentList = new ArrayList<>();

    public static EmailSendMessageEntity fromDomain(EmailSendMessage message) {
        EmailSendMessageEntity entity = new EmailSendMessageEntity(message);
        entity.assignSecurityMail(message.getSecurityMail());
        entity.convertMessage(message.getConvertMessage());
        return entity;
    }
    protected EmailSendMessageEntity(EmailSendMessage domain) {
        this.id = domain.getId();
        this.type = domain.getType().code();
        this.title = domain.getTitle();
        this.description = domain.getDescription();
        this.subject = domain.getSubject();
    }

    public void assignTemplate(EmailTemplateEntity template) {
        this.template = template;
    }

    private void convertMessage(ConvertMessage convertMessage) {
        if(convertMessage!=null) {
            MultiPartFile metadata = convertMessage.metadata();
            this.convertType = convertMessage.convertType().code();
            if(metadata != null) {
                this.attachmentName = metadata.attachmentName();
                this.description = metadata.downloadName();
                this.fileKeyTemplate = metadata.fileKeyTemplate();
            }
        }
    }

    private void assignSecurityMail(SecurityMail securityMail) {
        if(securityMail != null) {
            this.encryptionType = securityMail.encryptionType().code();
            this.permissionMask = securityMail.permissionMask().code();
            this.passwdHash = securityMail.passwdHash().code();
            this.passwdType = securityMail.passwdType().code();
        }
    }

    public EmailSendMessage toDomain() {
        return EmailSendMessage.fromEntity(this);
    }

    public ConvertMessage convertMessage() {
        if(!convertType.equals(ConvertType.NONE.code())) {
            MultiPartFile file = new MultiPartFile(this.fileKeyTemplate, this.attachmentName, this.downloadName);
            return new ConvertMessage(ConvertType.valueOf(convertType), file);
        }
        return new ConvertMessage(ConvertType.NONE, null);
    }

    public SecurityMail securityMail() {
        if(type.equals(EmailType.SECURITY.code())) {
            return new SecurityMail(
                    EncryptionTypeEnum.valueOf(encryptionType),
                    PasswordHashEnum.valueOf(passwdHash),
                    PermissionMaskEnum.valueOf(permissionMask),
                    PasswordType.valueOf(passwdType)
            );
        }
        return null;
    }
}