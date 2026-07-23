package com.ums.schedule.domain.message.email;

import com.ums.schedule.application.ums.email.message.provider.EmailMessageContext;
import com.ums.schedule.common.code.common.ChannelType;
import com.ums.schedule.common.code.email.ConvertType;
import com.ums.schedule.common.code.email.EmailRequiredValue;
import com.ums.schedule.domain.message.email.attachment.EmailAttachment;
import com.ums.schedule.domain.message.email.exception.EmailMessageValueMissingException;
import com.ums.schedule.domain.request.converter.UuidBinaryConverter;
import com.ums.schedule.domain.message.ChannelMessage;
import com.ums.schedule.domain.message.SendMessage;
import com.ums.schedule.common.code.email.EmailMessageSection;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.util.StringUtils;

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
        this.sendMessage = Objects.requireNonNull(sendMessage, "send_message");
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
                .orElseThrow(() -> EmailMessageValueMissingException.of(EmailRequiredValue.BODY_TEMPLATE_KEY));
    }

    private void assignBodyTemplate(String template) {
        if(!StringUtils.hasText(template)) {
            throw EmailMessageValueMissingException.of(EmailRequiredValue.BODY_TEMPLATE);
        }
        this.bodyTemplate = template;
    }

    private void assignFooterTemplate(String template) {

    }

    private void assignHeaderTemplate(String headerKey) {

    }

    private void assignSubject(SendMessage message, String title) {
        Objects.requireNonNull(title, "title");
        this.subject = message.generatePhraseByMessageType(title);
    }

    @Override
    public ChannelType channelType() {
        return ChannelType.EMAIL;
    }

    public void addAttachments(EmailAttachment attachment) {
        this.attachmentList.add(attachment);
    }

    public ConvertType findConvertType() {
        return attachmentList
                .stream()
                .filter(attachment -> attachment.getConvertType() != ConvertType.NONE)
                .map(EmailAttachment::getConvertType)
                .findFirst()
                .orElse(ConvertType.NONE);
    }

    public Integer getAttachmentCount() {
        return attachmentList.size();
    }
}