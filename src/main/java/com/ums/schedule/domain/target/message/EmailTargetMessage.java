package com.ums.schedule.domain.target.message;

import com.ums.schedule.application.sendrequest.target.data.TargetMessageData;
import com.ums.schedule.application.ums.email.generator.model.EmailTargetMessageCreateContext;
import com.ums.schedule.application.ums.email.generator.model.RenderedTemplateContent;
import com.ums.schedule.common.util.JsonUtil;
import com.ums.schedule.domain.message.email.EmailSendMessage;
import com.ums.schedule.domain.request.converter.UuidBinaryConverter;
import com.ums.schedule.domain.target.SendTarget;
import com.ums.schedule.domain.target.TargetMessage;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.io.IOException;
import java.io.StringWriter;
import java.util.*;

@Entity
@Getter
@DiscriminatorValue("EMAIL")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EmailTargetMessage extends TargetMessage {

    @Id
    @Column(columnDefinition = "BINARY(16)")
    @UuidGenerator
    @GeneratedValue
    @Convert(converter = UuidBinaryConverter.class)
    private UUID id;

    @Column(name = "subject", nullable = false)
    private String subject;

    @Column(name = "header_message")
    private String headerMessage;

    @Column(name = "body_message", nullable = false)
    private String bodyMessage;

    @Column(name = "footer_message")
    private String footerMessage;

    @Column(name = "attachments")
    private String attachments;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "message_id", nullable = false)
    private EmailSendMessage sendMessage;

    public static EmailTargetMessage of(EmailTargetMessageCreateContext context, TargetMessageData targetData) {
        EmailTargetMessage targetMessage = new EmailTargetMessage();
        targetMessage.renderTemplate(context, targetData);
        targetMessage.assignSendMessage(context.sendMessage());
        targetMessage.assignAttachmentJson(context.attachmentList());
        return targetMessage;
    }

    private void assignAttachmentJson(List<RenderedTemplateContent> attachments) {
        this.attachments = JsonUtil.toJson(attachments);
    }

    private void assignSendMessage(EmailSendMessage sendMessage) {
        Objects.requireNonNull(sendMessage, "email_send_message is not null.");
        this.sendMessage = sendMessage;
    }


    private void renderTemplate(EmailTargetMessageCreateContext context, TargetMessageData targetData) {
        this.subject = targetData.parse(context.subject());
        this.headerMessage = render(context.header(), targetData.getTargetParam());
        this.bodyMessage = context.bodyTemplate();
        this.footerMessage = render(context.footer(), targetData.getTargetParam());
    }

    private String render(Template template, Map<String, Object> targetParam) {
        if(template != null) {
            StringWriter writer = new StringWriter();
            try {
                template.process(targetParam, writer);
            } catch (TemplateException | IOException e) {
                e.printStackTrace();
            }
            return writer.toString();
        }
        return null;
    }

    @Override
    protected UUID messageId() {
        return sendMessage.getId();
    }
}
