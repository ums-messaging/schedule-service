package com.ums.schedule.fixture.email.convert;

import com.ums.schedule.application.ums.email.attachment.model.AttachmentContext;
import com.ums.schedule.application.ums.email.convert.strategy.model.EmailConvertPolicyContext;
import com.ums.schedule.application.ums.email.security.SecurityMail;
import com.ums.schedule.common.code.mapper.EnumMapperValue;
import com.ums.schedule.common.code.email.ConvertTypeEnum;

import java.util.List;

public class EmailConvertPolicyContextBuilder {
    private EnumMapperValue convertType;
    private SecurityMail securityMail;
    private AttachmentContext body;
    private String coverKey;
    private List<AttachmentContext> attachments;

    public static EmailConvertPolicyContextBuilder builder() {
        return new EmailConvertPolicyContextBuilder();
    }

    private EmailConvertPolicyContextBuilder() {
        this.convertType = EnumMapperValue.fromEnumMapperType(ConvertTypeEnum.NONE);
        this.coverKey = "cover.html";
        this.attachments = List.of();
    }

    public EmailConvertPolicyContextBuilder convertType(EnumMapperValue convertType) {
        this.convertType = convertType;
        return this;
    }

    public EmailConvertPolicyContextBuilder securityMail(SecurityMail securityMail) {
        this.securityMail = securityMail;
        return this;
    }

    public EmailConvertPolicyContextBuilder body(AttachmentContext body) {
        this.body = body;
        return this;
    }

    public EmailConvertPolicyContextBuilder coverKey(String coverKey) {
        this.coverKey = coverKey;
        return this;
    }

    public EmailConvertPolicyContextBuilder attachments(List<AttachmentContext> attachments) {
        this.attachments = attachments;
        return this;
    }

    public EmailConvertPolicyContext build() {
        return new EmailConvertPolicyContext(
                convertType,
                securityMail,
                body,
                coverKey
        );
    }
}
