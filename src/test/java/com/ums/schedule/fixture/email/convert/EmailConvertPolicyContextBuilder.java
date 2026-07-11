package com.ums.schedule.fixture.email.convert;

import com.ums.schedule.application.ums.email.convert.ConvertedAttachment;
import com.ums.schedule.application.ums.email.convert.strategy.model.EmailConvertPolicyContext;
import com.ums.schedule.common.code.mapper.EnumMapperValue;
import com.ums.schedule.domain.sendrequest.resource.email.code.ConvertTypeEnum;

import java.util.List;

public class EmailConvertPolicyContextBuilder {
    private EnumMapperValue convertType;
    private String bodyKey;
    private String coverKey;
    private List<ConvertedAttachment> attachments;

    public static EmailConvertPolicyContextBuilder builder() {
        return new EmailConvertPolicyContextBuilder();
    }

    private EmailConvertPolicyContextBuilder() {
        this.convertType = EnumMapperValue.fromEnumMapperType(ConvertTypeEnum.NONE);
        this.bodyKey = "body.html";
        this.coverKey = "cover.html";
        this.attachments = List.of();
    }

    public EmailConvertPolicyContextBuilder convertType(EnumMapperValue convertType) {
        this.convertType = convertType;
        return this;
    }

    public EmailConvertPolicyContextBuilder bodyKey(String bodyKey) {
        this.bodyKey = bodyKey;
        return this;
    }

    public EmailConvertPolicyContextBuilder coverKey(String coverKey) {
        this.coverKey = coverKey;
        return this;
    }

    public EmailConvertPolicyContextBuilder attachments(List<ConvertedAttachment> attachments) {
        this.attachments = attachments;
        return this;
    }

    public EmailConvertPolicyContext build() {
        return new EmailConvertPolicyContext(
                convertType,
                bodyKey,
                coverKey,
                attachments
        );
    }
}
