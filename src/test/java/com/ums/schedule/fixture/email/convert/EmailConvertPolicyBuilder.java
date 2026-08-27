package com.ums.schedule.fixture.email.convert;

import com.ums.schedule.application.ums.email.generator.policy.model.EmailConvertPolicy;
import com.ums.schedule.common.code.email.ConvertType;
import com.ums.schedule.domain.target.message.AttachmentPayload;
import freemarker.template.Template;

import java.util.Arrays;
import java.util.List;

public class EmailConvertPolicyBuilder {
    private ConvertType convertType;
    private Template bodyTemplate;
    private List<AttachmentPayload> attachments;

    public static EmailConvertPolicyBuilder builder() {
        return new EmailConvertPolicyBuilder();
    }

    private EmailConvertPolicyBuilder()  {
        this.convertType = ConvertType.PDF;
    }

    public EmailConvertPolicyBuilder body(Template bodyTemplate) {
        this.bodyTemplate = bodyTemplate;
        return this;
    }

    public EmailConvertPolicyBuilder attachments(AttachmentPayload... contents) {
        this.attachments = Arrays.asList(contents);
        return this;
    }

    public EmailConvertPolicy build() {
        return new EmailConvertPolicy(
                convertType,
                bodyTemplate,
                attachments
        );
    }
}
