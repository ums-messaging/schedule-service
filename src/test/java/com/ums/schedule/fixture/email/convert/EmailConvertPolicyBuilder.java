package com.ums.schedule.fixture.email.convert;

import com.ums.schedule.application.ums.email.generator.AttachmentMetadata;
import com.ums.schedule.application.ums.email.generator.model.RenderedTemplateContent;
import com.ums.schedule.application.ums.email.generator.policy.model.EmailConvertPolicy;
import com.ums.schedule.common.code.email.ConvertType;
import com.ums.schedule.common.code.mapper.EnumMapperValue;

import java.util.Arrays;
import java.util.List;

public class EmailConvertPolicyBuilder {
    private ConvertType convertType;
    private String bodyTemplate;
    private List<RenderedTemplateContent> attachments;

    public static EmailConvertPolicyBuilder builder() {
        return new EmailConvertPolicyBuilder();
    }

    private EmailConvertPolicyBuilder() {
        this.convertType = ConvertType.PDF;
        this.bodyTemplate = "body template.";
    }

    public EmailConvertPolicyBuilder bodyTemplate(String bodyTemplate) {
        this.bodyTemplate = bodyTemplate;
        return this;
    }

    public EmailConvertPolicyBuilder attachments(RenderedTemplateContent... contents) {
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
