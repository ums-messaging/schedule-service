package com.ums.schedule.domain.message.exception;

import com.ums.schedule.application.message.email.model.AttachmentCreateCommand;
import com.ums.schedule.application.message.email.model.EmailSendMessageCreateCommand;
import com.ums.schedule.application.ums.email.template.command.EmailTemplateContentCommand;
import com.ums.schedule.common.util.ValidationUtils;
import com.ums.schedule.domain.sendrequest.converter.UuidBinaryConverter;
import com.ums.schedule.domain.message.email.EmailAttachment;
import com.ums.schedule.domain.sendrequest.template.email.code.EmailTemplateSectionEnum;
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
@AllArgsConstructor
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

    public static EmailSendMessage of(EmailSendMessageCreateCommand command) {
        EmailSendMessage sendMessage = new EmailSendMessage();
        sendMessage.assignSubject(command.sendMessage(), command.title());
        sendMessage.assignTemplateInfo(command.headerKey(), command.bodyKey(), command.footerKey());
        sendMessage.generateImageDir();
        return sendMessage;
    }


    private void assignTemplateInfo(String headerKey, String bodyKey, String footerKey) {
        assignHeaderFooterTemplateKey(headerKey, footerKey);
        assignBodyTemplateKey(bodyKey);
    }

    private void assignHeaderFooterTemplateKey(String headerKey, String footerKey) {
        assignHeaderTemplate(headerKey);
        assignFooterTemplate(footerKey);
    }

    private void assignBodyTemplateKey(String body) {

    }

    private void assignBodyTemplate(String template) {
        if(!StringUtils.hasText(template)) {
            throw EmailMessageMissingException.bodyOf();
        }
        this.bodyTemplate = template;
    }

    private void assignFooterTemplate(String template) {

    }

    private void assignHeaderTemplate(String headerKey) {

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