package com.ums.schedule.email.domain.message;

import com.github.f4b6a3.uuid.UuidCreator;
import com.ums.schedule.common.code.email.ConvertType;
import com.ums.schedule.common.code.email.EmailType;
import com.ums.schedule.common.code.message.MessageType;
import com.ums.schedule.email.adaptor.out.persistence.jpa.entity.EmailSendMessageEntity;
import com.ums.schedule.email.application.message.command.EmailSendMessageCreateCommand;
import com.ums.schedule.email.domain.attachment.EmailAttachment;
import com.ums.schedule.email.domain.common.MultiPartFile;
import com.ums.schedule.email.domain.message.security.SecurityMail;
import com.ums.schedule.email.domain.template.EmailTemplate;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EmailSendMessage {
    private UUID id;
    private String title;
    private String description;

    private String subject;

    private MessageType messageType;
    private EmailType type;
    private SecurityMail securityMail;

    private ConvertMessage convertMessage;
    private String templateId;
    private String customerId;

    public static EmailSendMessage of(EmailSendMessageCreateCommand command) {
        EmailSendMessage message = new EmailSendMessage(command);
        message.assignSubject(command.messageType(), command.subject());
        message.assignSecurityMail(command.type(), command.securityMail());
        message.assignConvertMessage(command.convertMessage());
        return message;
    }
    protected EmailSendMessage(EmailSendMessageCreateCommand command) {
        this.title = command.title();
        this.description = command.description();
        this.messageType = command.messageType();
        this.templateId = command.templateId();
        this.customerId = command.customerId();
    }

    private void assignSubject(MessageType messageType, String subject) {
        if(messageType == MessageType.ADVERTISE) {
            this.subject = messageType.description() + " "+ subject;
            return;
        }
        this.subject = subject;
    }

    private void assignConvertMessage(ConvertMessage convertMessage) {
        this.convertMessage = convertMessage;
    }

    private ConvertType resolveConvertType(EmailType type, ConvertType convertType) {
        if(type == EmailType.SECURITY && convertType == ConvertType.NONE) {
            return ConvertType.HTML;
        }
        return convertType;
    }

    private void assignSecurityMail(EmailType type, SecurityMail securityMail) {
        this.type = type;
        if(type == EmailType.SECURITY) {
            this.securityMail = Optional.ofNullable(securityMail).orElseThrow();
            return;
        }
    }

    public EmailSendMessageEntity toEntity() {
        return EmailSendMessageEntity.fromDomain(this);
    }

    public static EmailSendMessage fromEntity(EmailSendMessageEntity entity) {
        return new EmailSendMessage(entity);
    }

    private EmailSendMessage(EmailSendMessageEntity entity) {
        EmailType type = EmailType.valueOf(entity.getType());
        this.id = entity.getId();
        this.type = type;
        this.subject = entity.getSubject();

        this.title = entity.getTitle();
        this.description = entity.getDescription();

        this.templateId = UuidCreator.toString(entity.getTemplate().getId());
        this.securityMail = entity.securityMail();
        this.convertMessage = entity.convertMessage();
    }
}
