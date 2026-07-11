package com.ums.schedule.domain.sendrequest.message.email;

import com.ums.schedule.application.message.email.model.AttachmentCreateCommand;
import com.ums.schedule.application.message.email.model.EmailSendMessageCreateCommand;
import com.ums.schedule.application.ums.email.template.command.EmailTemplateContentCommand;
import com.ums.schedule.common.util.ValidationUtils;
import com.ums.schedule.domain.sendrequest.converter.UuidBinaryConverter;
import com.ums.schedule.domain.sendrequest.resource.email.EmailAttachment;
import com.ums.schedule.domain.sendrequest.template.email.code.EmailTemplateSectionEnum;
import com.ums.schedule.domain.sendrequest.message.email.exception.EmailMessageFileKeyMissingException;
import com.ums.schedule.domain.sendrequest.message.email.exception.EmailMessageMissingException;
import com.ums.schedule.domain.sendrequest.message.ChannelMessage;
import com.ums.schedule.domain.sendrequest.message.SendMessage;
import com.ums.schedule.domain.sendrequest.code.ChannelTypeEnum;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.util.StringUtils;

import java.io.File;
import java.util.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class EmailSendMessage implements ChannelMessage {

    @MapsId
    @OneToOne
    @JoinColumn(name = "message_id")
    private SendMessage sendMessage;

    @Id
    @Column(columnDefinition = "BINARY(16)")
    @UuidGenerator
    @GeneratedValue
    @Convert(converter = UuidBinaryConverter.class)
    private UUID id;

    private String subject;

    private String headerTemplateKey;
    private String headerTemplate;

    private String bodyTemplateKey;
    private String bodyTemplate;

    private String footerTemplateKey;
    private String footerTemplate;

    private String imageDir;

    @OneToMany(mappedBy = "sendMessage", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private List<EmailAttachment> attachmentList = new ArrayList<>();

    public static EmailSendMessage of(SendMessage message, EmailSendMessageCreateCommand command, List<AttachmentCreateCommand> attachments) {
        EmailSendMessage sendMessage = new EmailSendMessage();
        sendMessage.assignSubject(message, command.title());
        sendMessage.assignTemplateInfo(command.templateMap());
        sendMessage.generateImageDir();
        sendMessage.createAttachmentList(attachments);
        return sendMessage;
    }

    private void createAttachmentList(List<AttachmentCreateCommand> attachments) {
       this.attachmentList = attachments.stream()
               .map(attachment -> EmailAttachment.of(this, attachment))
               .toList();
    }

    private void assignTemplateInfo(Map<EmailTemplateSectionEnum, EmailTemplateContentCommand> contentMap) {
        assignHeaderFooterTemplateKey(contentMap);
        assignBodyTemplateKey(contentMap.get(EmailTemplateSectionEnum.BODY));
    }

    private void assignHeaderFooterTemplateKey(Map<EmailTemplateSectionEnum, EmailTemplateContentCommand> headerFooter) {
        assignHeaderTemplate(headerFooter.get(EmailTemplateSectionEnum.HEADER));
        assignFooterTemplate(headerFooter.get(EmailTemplateSectionEnum.FOOTER));
    }

    private void assignBodyTemplateKey(EmailTemplateContentCommand template) {
        if(template != null) {
            if(!StringUtils.hasText(template.fileKey())) {
                throw EmailMessageFileKeyMissingException.bodyOf();
            }
            this.bodyTemplateKey = template.fileKey();
            assignBodyTemplate(template.template());
            return;
        }
        throw EmailMessageMissingException.bodyOf();
    }

    private void assignBodyTemplate(String template) {
        if(!StringUtils.hasText(template)) {
            throw EmailMessageMissingException.bodyOf();
        }
        this.bodyTemplate = template;
    }

    private void assignFooterTemplate(EmailTemplateContentCommand template) {
        if(template != null) {
            if(StringUtils.hasText(template.fileKey())) {
                if(!StringUtils.hasText(template.template())) {
                    throw EmailMessageMissingException.footerOf();
                }
                this.footerTemplateKey = template.fileKey();
                this.footerTemplate = template.template();
                return;
            }
        }
        this.footerTemplate = null;
    }

    private void assignHeaderTemplate(EmailTemplateContentCommand template) {
        if(template != null) {
            if(StringUtils.hasText(template.fileKey())) {
                if(!StringUtils.hasText(template.template())) {
                    throw EmailMessageMissingException.headerOf();
                }
                this.headerTemplateKey = template.fileKey();
                this.headerTemplate = template.template();
                return;
            }
        }
        this.headerTemplate = null;
    }

    private void generateImageDir() {
        String[] fileSeparators = bodyTemplateKey.split("\\/");
        String extractDir = bodyTemplateKey.substring(0, fileSeparators.length - 1);
        this.imageDir = extractDir + File.separator + "images";
    }


    private void assignSubject(SendMessage message, String title) {
        ValidationUtils.isEmpty("title", title);
        this.subject = message.generatePhraseByMessageType(title);
    }

    @Override
    public ChannelTypeEnum channelType() {
        return ChannelTypeEnum.EMAIL;
    }
}