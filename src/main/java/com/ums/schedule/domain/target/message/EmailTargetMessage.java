package com.ums.schedule.domain.target.message;

import com.ums.schedule.application.target.uploader.model.EmailGeneratorContext;
import com.ums.schedule.application.ums.common.target.result.SendTargetResult;
import com.ums.schedule.application.ums.common.template.loader.model.EmailTemplate;
import com.ums.schedule.application.ums.email.generator.policy.model.EmailConvertPolicy;
import com.ums.schedule.common.code.email.ConvertType;
import com.ums.schedule.common.code.target.SendTargetColumn;
import com.ums.schedule.common.code.target.SendTargetResultCode;
import com.ums.schedule.common.code.target.SendTargetStatus;
import com.ums.schedule.common.util.JsonUtil;
import com.ums.schedule.domain.message.email.EmailSendMessage;
import com.ums.schedule.domain.target.TargetMessage;
import com.ums.schedule.domain.target.upload.TargetUploadReport;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.flywaydb.core.internal.util.StringUtils;

import java.io.IOException;
import java.io.StringWriter;
import java.util.*;

@Entity
@Getter
@AllArgsConstructor
@DiscriminatorValue("EMAIL")
@Table(uniqueConstraints = {
        @UniqueConstraint(
                name="uq_target_key",
                columnNames = {"upload_id", "target_key"}
        ),
        @UniqueConstraint(
                name = "uq_target_contact",
                columnNames = {"upload_id", "contact"}
        )
    }
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EmailTargetMessage extends TargetMessage {
    @Column(name = "email_domain")
    private String domain;

    @Column(name = "subject")
    private String subject;

    @Column(name = "header_message")
    private String headerMessage;

    @Column(name = "body_message")
    private String bodyMessage;

    @Column(name = "footer_message")
    private String footerMessage;

    private String attachments;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "message_id", nullable = false)
    private EmailSendMessage sendMessage;

    public static EmailTargetMessage of(EmailGeneratorContext context, SendTargetResult target) {
        EmailTargetMessage targetMessage = new EmailTargetMessage(context.targetUploadReport(), target);
        targetMessage.assignSendMessage(context.sendMessage());
        return targetMessage;
    }
    protected EmailTargetMessage(TargetUploadReport targetUploadReport, SendTargetResult target) {
        super(targetUploadReport, target);
    }
    private void parseToPayload(List<AttachmentPayload> attachments) {
        String json = JsonUtil.toJson(attachments);
        if(StringUtils.hasText(json)) {
            this.attachments = parseTemplate("attachment", json);
        }
    }

    private void assignSendMessage(EmailSendMessage sendMessage) {
        Objects.requireNonNull(sendMessage, "email_send_message is not null.");
        this.sendMessage = sendMessage;
    }
    private String renderBody(EmailTemplate template, Map<String, Object> dataParam) {
        if(template.getConvertType() != ConvertType.NONE) {
            return render("body", template.getCoverTemplate(), dataParam);
        }
        return render("body", template.getBodyTemplate(), dataParam);
    }

    public String render(String field, Template template, Map<String, Object> dataParam) {
        if(template != null) {
            StringWriter writer = new StringWriter();
            try {
                template.process(dataParam, writer);
                return writer.toString();
            } catch (TemplateException | IOException e) {
                onError(SendTargetResultCode.MESSAGE_PARSING_ERROR, field, e.getMessage());
            }
        }
        return null;
    }

    private String parseTemplate(String field, String template) {
        return parse(field, template);
    }

    @Override
    protected UUID messageId() {
        return sendMessage.getId();
    }

    @Override
    protected String assignContact(Map<SendTargetColumn, String> targetMap) {
        return assignTargetData(SendTargetColumn.TARGET_EMAIL, targetMap.get(SendTargetColumn.TARGET_EMAIL));
    }

    public void renderTemplate(EmailTemplate template, EmailConvertPolicy policy) {
        if(this.state.getCurrentCode() == SendTargetStatus.CREATE) {
            renderTemplate(template, policy, getDataParamMap());
            parseToPayload(policy.attachments());
        }
    }

    private void renderTemplate(EmailTemplate template, EmailConvertPolicy policy, Map<String, Object> dataParam) {
        this.subject = parseTemplate("subject", template.getTitle());
        this.headerMessage = render("header",template.getHeaderTemplate(), dataParam);
        this.bodyMessage = renderBody(policy, dataParam);
        this.footerMessage = render("footer", template.getFooterTemplate(), dataParam);
    }

    private String renderBody(EmailConvertPolicy policy, Map<String, Object> dataParam) {
        if(policy.bodyTemplate() == null) {
            onError(SendTargetResultCode.TEMPLATE_EMPTY, "body");
            return null;
        }
        return render("body", policy.bodyTemplate(), dataParam);
    }
}
