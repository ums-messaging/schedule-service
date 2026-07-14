package com.ums.schedule.domain.message.email;

import com.ums.schedule.application.ums.email.message.provider.EmailMessageContext;
import com.ums.schedule.common.util.ValidationUtils;
import com.ums.schedule.domain.message.exception.EmailMessageMissingException;
import com.ums.schedule.domain.message.exception.EmailMessageTemplateFileKeyMissingException;
import com.ums.schedule.domain.sendrequest.converter.UuidBinaryConverter;
import com.ums.schedule.domain.sendrequest.exception.SendMessageNotFoundException;
import com.ums.schedule.domain.sendrequest.message.ChannelMessage;
import com.ums.schedule.domain.sendrequest.message.SendMessage;
import com.ums.schedule.domain.sendrequest.code.ChannelTypeEnum;
import com.ums.schedule.domain.sendrequest.template.email.code.EmailTemplateSectionEnum;
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

    public static EmailSendMessage of(SendMessage message, EmailMessageContext context) {
        EmailSendMessage sendMessage = new EmailSendMessage();
        sendMessage.assignTemplateKeyInfo(context.headerKey(), context.bodyKey(), context.footerKey());
        sendMessage.assignSendMessageAndResolveTitle(message, context.title());
        sendMessage.assignImageDir(context.imageDir());
        return sendMessage;
    }

    private void assignImageDir(String imageDir) {
        this.imageDir = imageDir;
    }

    private void assignSendMessageAndResolveTitle(SendMessage sendMessage, String title) {
        this.sendMessage = Optional.ofNullable(sendMessage)
                .orElseThrow(SendMessageNotFoundException::of);
        assignSubject(sendMessage, title);
    }

    private void assignTemplateKeyInfo(String headerKey, String bodyKey, String footerKey) {
        assignHeaderFooterTemplateKey(headerKey, footerKey);
        assignBodyTemplateKey(bodyKey);
    }

    private void assignHeaderFooterTemplateKey(String headerKey, String footerKey) {
        assignHeaderTemplateKey(headerKey);
        assignFooterTemplateKey(footerKey);
    }

    private void assignFooterTemplateKey(String footerKey) {
        this.footerTemplateKey = footerKey;
    }

    private void assignHeaderTemplateKey(String headerKey) {
        this.headerTemplateKey = headerKey;
    }

    private void assignBodyTemplateKey(String bodyKey) {
        this.bodyTemplateKey = Optional.ofNullable(bodyKey)
                .filter(StringUtils::hasText)
                .orElseThrow(() -> EmailMessageTemplateFileKeyMissingException.of(EmailTemplateSectionEnum.BODY));
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


    private void assignSubject(SendMessage message, String title) {
        ValidationUtils.isEmpty("subject", title);
        this.subject = message.generatePhraseByMessageType(title);
    }

    @Override
    public ChannelTypeEnum channelType() {
        return ChannelTypeEnum.EMAIL;
    }

    public void addAttachments(EmailAttachment attachment) {
        this.attachmentList.add(attachment);
    }
}