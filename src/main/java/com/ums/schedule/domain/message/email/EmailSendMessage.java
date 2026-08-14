package com.ums.schedule.domain.message.email;

import com.ums.schedule.application.ums.email.message.model.EmailMessageCreateContext;
import com.ums.schedule.application.ums.email.message.provider.EmailPolicyResult;
import com.ums.schedule.application.ums.email.security.SecurityMail;
import com.ums.schedule.common.code.common.ChannelType;
import com.ums.schedule.common.code.email.ConvertType;
import com.ums.schedule.common.code.email.EmailMessageSection;
import com.ums.schedule.common.code.email.EmailRequiredValue;
import com.ums.schedule.common.code.email.EmailType;
import com.ums.schedule.domain.message.email.attachment.EmailAttachment;
import com.ums.schedule.domain.message.email.convert.ConvertMail;
import com.ums.schedule.domain.message.email.exception.EmailMessageValueMissingException;
import com.ums.schedule.domain.message.email.security.SecurityMailPolicy;
import com.ums.schedule.domain.request.converter.UuidBinaryConverter;
import com.ums.schedule.domain.message.ChannelMessage;
import com.ums.schedule.domain.message.SendMessage;
import com.ums.schedule.domain.target.message.EmailTargetMessage;
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
    @JoinColumn(name = "message_id", nullable = false)
    private SendMessage sendMessage;

    @Id
    @Column(columnDefinition = "BINARY(16)")
    @UuidGenerator
    @GeneratedValue
    @Convert(converter = UuidBinaryConverter.class)
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(name = "email_type", nullable = false)
    private EmailType emailType;

    @Column(name = "subject", nullable = false)
    private String subject;

    private String headerTemplateKey;

    @Column(name = "body_template_key", nullable = false)
    private String bodyTemplateKey;

    private String coverTemplateKey;
    private String footerTemplateKey;
    private String imageDir;

    @OneToMany(mappedBy = "sendMessage", fetch = FetchType.LAZY)
    private List<EmailAttachment> attachments = new ArrayList<>();

    @Embedded
    private SecurityMailPolicy securityMail;

    @Embedded
    private ConvertMail convertMail;


    public static EmailSendMessage of(SendMessage message, EmailMessageCreateContext context) {
        EmailSendMessage sendMessage = new EmailSendMessage();
        sendMessage.assignTemplateKeyInfo(context.headerKey(), context.bodyKey(), context.footerKey());
        sendMessage.assignSendMessageAndResolveTitle(message, context.title());
        sendMessage.assignImageDir(context.imageDir());
        sendMessage.assignEmailType(context.emailType());
        sendMessage.assignSecurityMailPolicy(context.securityMail());
        sendMessage.assignConvertPolicy(context.convertMail());
        return sendMessage;
    }

    private void assignEmailType(EmailType emailType) {
        this.emailType = Objects.requireNonNull(emailType, "emailType is required.");
    }

    private void assignSecurityMailPolicy(SecurityMail securityMail) {
        this.securityMail = Optional.ofNullable(securityMail)
                .map(SecurityMailPolicy::of)
                .orElse(null);
    }

    private void assignConvertPolicy(ConvertMail convertMail) {
        this.convertMail = Optional.ofNullable(convertMail)
                .orElseGet(() -> ConvertMail.of());
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

    private void assignSubject(SendMessage message, String title) {
        Objects.requireNonNull(title, "title");
        this.subject = message.generatePhraseByMessageType(title);
    }

    @Override
    public ChannelType channelType() {
        return ChannelType.EMAIL;
    }

    public Map<EmailMessageSection, String> mapToTemplateKey() {
        Map<EmailMessageSection, String> templateMap = new EnumMap<>(EmailMessageSection.class);
        putIfKeyExists(templateMap, EmailMessageSection.HEADER, this.headerTemplateKey);
        putIfKeyExists(templateMap, EmailMessageSection.FOOTER, this.footerTemplateKey);
        putIfKeyExists(templateMap, EmailMessageSection.BODY, this.bodyTemplateKey);
        putIfKeyExists(templateMap, EmailMessageSection.COVER, this.coverTemplateKey);
        return templateMap;
    }

    private void putIfKeyExists(Map<EmailMessageSection, String> templateMap, EmailMessageSection section, String fileKey) {
        if(StringUtils.hasText(fileKey)) {
            templateMap.put(section, fileKey);
        }
    }

    private String validateAndGetCoverKey() {
        return Optional
                .ofNullable(convertMail)
                .map(v -> validateTemplateKey(this.coverTemplateKey))
                .orElse(null);
    }

    private String validateTemplateKey(String templateKey) {
        return Optional.ofNullable(templateKey)
                .filter(StringUtils::hasText)
                .orElseThrow();
    }

    public ConvertType getConvertType() {
        return Optional.ofNullable(this.convertMail)
                .map(ConvertMail::getConvertType)
                .orElseGet(() -> ConvertType.NONE);
    }

    public void addAttachments(EmailAttachment attachment) {
        this.attachments.add(attachment);
    }

    public Integer getAttachmentCount() {
        return this.attachments.size();
    }

}