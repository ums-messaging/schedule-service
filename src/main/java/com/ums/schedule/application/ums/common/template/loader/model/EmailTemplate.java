package com.ums.schedule.application.ums.common.template.loader.model;

import com.ums.schedule.application.sendrequest.target.data.TargetMessageData;
import com.ums.schedule.application.ums.common.exception.TemplateParseException;
import com.ums.schedule.application.ums.email.security.SecurityMail;
import com.ums.schedule.common.code.email.ConvertType;
import com.ums.schedule.common.code.email.EmailMessageSection;
import com.ums.schedule.common.code.email.EmailType;
import com.ums.schedule.common.code.mapper.EnumMapperValue;
import com.ums.schedule.domain.message.email.EmailSendMessage;
import com.ums.schedule.domain.message.email.attachment.EmailAttachment;
import com.ums.schedule.domain.message.email.convert.ConvertMail;
import com.ums.schedule.domain.target.message.AttachmentPayload;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.StringWriter;
import java.util.*;


@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class EmailTemplate implements ChannelTemplate  {

    private String templateKey;
    private EmailType emailType;
    private ConvertType convertType;
    private String title;
    private SecurityMail securityMail;
    private EmailTemplateContent header;
    private EmailTemplateContent cover;
    private EmailTemplateContent body;
    private EmailTemplateContent footer;

    private List<EmailTemplateContent> attachments = new ArrayList<>();


    public static EmailTemplate of(EmailSendMessage sendMessage, Map<EmailMessageSection, Template> templateMap) {
        EmailTemplate template = new EmailTemplate();
        template.assignTemplateKey(sendMessage.getSendMessage().getTemplateKey());
        template.assignTitle(sendMessage.getSubject());
        template.assignConvertType(sendMessage.getConvertType());
        template.assignMessage(templateMap, sendMessage);
        template.assignAttachments(sendMessage.getAttachments());
        template.assignSecurityMail(sendMessage.getEmailType(), sendMessage);
        return template;
    }

    private void assignTemplateKey(String templateKey) {
        Objects.requireNonNull(templateKey, "template_key is not null");
        this.templateKey = templateKey;
    }

    private void assignConvertType(ConvertType convertType) {
        Objects.requireNonNull(convertType, "convert_type is not null");
        this.convertType = convertType;
    }

    private void assignSecurityMail(EmailType type, EmailSendMessage sendMessage) {
        if(type == EmailType.SECURITY) {
            if(sendMessage.getConvertType() == ConvertType.NONE) {
                throw new IllegalArgumentException("convert_type is not none.");
            }
            Objects.requireNonNull(securityMail, "security_mail_policy is not null.");

            this.securityMail = Optional.ofNullable(sendMessage.getSecurityMail())
                    .map(SecurityMail::fromEntity)
                    .orElseThrow();
        }
        assignEmailType(type);
    }

    private void assignEmailType(EmailType emailType) {
        this.emailType = emailType;
    }

    private void assignAttachments(List<EmailAttachment> attachments) {
        this.attachments = attachments.stream()
                .map(attachment -> EmailTemplateContent.of(attachment))
                .toList();
    }

    public String renderTemplate(Template template, TargetMessageData targetMessageData) {
        return Optional.ofNullable(template)
                .map(v -> render(v, targetMessageData.getTargetParam()))
                .map(StringWriter::toString)
                .orElse(null);
    }

    private StringWriter render(Template template, Map<String, Object> dataParam) {
        try {
            StringWriter writer = new StringWriter();
            template.process(dataParam, writer);
            return writer;
        } catch (IOException | TemplateException e) {
            throw TemplateParseException.of(String.valueOf(dataParam.get("targetKey")), e);
        }
    }

    private void assignMessage(Map<EmailMessageSection, Template> templateMap, EmailSendMessage sendMessage)  {
        assignHeaderTemplate(templateMap, sendMessage.getHeaderTemplateKey());
        assignFooterTemplate(templateMap, sendMessage.getFooterTemplateKey());
        assignBodyTemplate(templateMap, sendMessage);
    }

    private void assignBodyTemplate(Map<EmailMessageSection, Template> templateMap, EmailSendMessage sendMessage) {
        ConvertType convertType = sendMessage.getConvertType();
        Objects.requireNonNull(sendMessage.getBodyTemplateKey(), "body_file_key");
        Objects.requireNonNull(templateMap.get(EmailMessageSection.BODY), "body_template is not null.");

        if(convertType != ConvertType.NONE) {
            assignMessageByConvertType(templateMap, sendMessage.getConvertMail(), sendMessage.getCoverTemplateKey());
            return;
        }
        this.body = toEmailContent(templateMap.get(EmailMessageSection.BODY));
    }

    private void assignMessageByConvertType(Map<EmailMessageSection, Template> templateMap, ConvertMail convertMail, String coverKey) {
        Objects.requireNonNull(convertMail.getAttachmentName(), "[body_template] attachment_name is required.");
        Objects.requireNonNull(convertMail.getDownloadName(), "[body_template] download_name is required.");
        assignCoverTemplate(templateMap, coverKey);
    }

    private void assignCoverTemplate(Map<EmailMessageSection, Template> templateMap, String fileKey) {
        Objects.requireNonNull(fileKey, "cover_file_key is required.");
        Objects.requireNonNull(templateMap.get(EmailMessageSection.COVER), "cover_template is not null");
        this.cover = toEmailContent(templateMap.get(EmailMessageSection.COVER));
    }

    private void assignFooterTemplate(Map<EmailMessageSection, Template> templateMap, String templateKey) {
        if(StringUtils.hasText(templateKey)) {
            Objects.requireNonNull(templateMap.get(EmailMessageSection.FOOTER), "footer_template");
            this.footer = toEmailContent(templateMap.get(EmailMessageSection.FOOTER));
        }
    }

    private void assignHeaderTemplate(Map<EmailMessageSection, Template> templateMap, String templateKey) {
        if(StringUtils.hasText(templateKey)) {
            Objects.requireNonNull(templateMap.get(EmailMessageSection.HEADER), "header_template not null.");
            this.header = toEmailContent(templateMap.get(EmailMessageSection.HEADER));
        }
    }

    private EmailTemplateContent toEmailContent(Template template) {
        return Optional.ofNullable(template)
                .map(EmailTemplateContent::of)
                .orElse(null);
    }

    public void assignTitle(String title) {
        Objects.requireNonNull(title, "title is required.");
        this.title = title;
    }

    public String passwordPolicy() {
        if(isSecurityMail()) {
            return Optional.ofNullable(securityMail)
                    .map(SecurityMail::passwordPolicy)
                    .orElseThrow();
        }
        return null;
    }

    private boolean isSecurityMail() {
        return emailType == EmailType.SECURITY;
    }

    public Template getHeaderTemplate() {
        return getTemplate(this.header);
    }

    public Template getFooterTemplate() {
        return getTemplate(this.footer);
    }

    public Template getCoverTemplate() {
        return getTemplate(this.cover);
    }

    public Template getBodyTemplate() {
        return getTemplate(this.body);
    }

    private Template getTemplate(EmailTemplateContent templateContent) {
        return Optional.ofNullable(templateContent)
                .map(EmailTemplateContent::template)
                .orElse(null);
    }

    public EnumMapperValue convertTypeValue() {
        return EnumMapperValue.fromEnumMapperType(this.convertType);
    }

    public List<AttachmentPayload> toPayloads() {
        return attachments.stream()
                .map(attachment -> {
                    if(StringUtils.hasText(attachment.fileKey())) {
                        return AttachmentPayload.of(attachment,attachment.fileKey());
                    }
                    return AttachmentPayload.of(attachment, attachment.fileKeyTemplate());
                })
                .toList();
    }

}
