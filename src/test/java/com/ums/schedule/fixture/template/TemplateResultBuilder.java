package com.ums.schedule.fixture.template;

import com.ums.schedule.application.ums.common.template.model.TemplateResult;

import java.util.UUID;

public class TemplateResultBuilder {
    private String templateId;
    private String templateName;
    private String templateType;
    private String channelType;

    public static TemplateResultBuilder builder() {
        return new TemplateResultBuilder();
    }

    private TemplateResultBuilder() {
        this.templateId = UUID.randomUUID().toString();
        this.templateName = "my_template";
        this.templateType = "ADVERTISE";
        this.channelType = "EMAIL";
    }

    public TemplateResultBuilder templateId(String templateId) {
        this.templateId = templateId;
        return this;
    }

    public TemplateResultBuilder templateName(String templateName) {
        this.templateName = templateName;
        return this;
    }

    public TemplateResultBuilder templateType(String templateType) {
        this.templateType = templateType;
        return this;
    }

    public TemplateResultBuilder channelType(String channelType) {
        this.channelType = channelType;
        return this;
    }

    public TemplateResult build() {
        return new TemplateResult(
                this.templateId,
                this.templateName,
                this.templateType,
                this.channelType
        );
    }
}
