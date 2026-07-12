package com.ums.schedule.fixture.email.convert;

import com.ums.schedule.application.ums.email.convert.ConvertedAttachment;
import com.ums.schedule.application.ums.email.convert.EmailConvertPolicy;
import com.ums.schedule.common.code.mapper.EnumMapperValue;
import com.ums.schedule.domain.message.email.SecurityMailPolicy;
import com.ums.schedule.domain.message.email.code.ConvertTypeEnum;

public class EmailConvertPolicyBuilder {
    private EnumMapperValue convertType;
    private String bodyKey;
    private SecurityMailPolicy securityMailPolicy;
    private ConvertedAttachment convertedAttachment;

    public static EmailConvertPolicyBuilder builder() {
        return new EmailConvertPolicyBuilder();
    }

    private EmailConvertPolicyBuilder() {
        this.convertType = EnumMapperValue.fromEnumMapperType(ConvertTypeEnum.PDF);
        this.bodyKey = "body.html";
        this.convertedAttachment = ConvertedAttachmentBuilder.builder().build();
    }

    public EmailConvertPolicyBuilder convertedAttachment(ConvertedAttachment convertedAttachment) {
        this.convertedAttachment = convertedAttachment;
        return this;
    }

    public EmailConvertPolicy build() {
        return new EmailConvertPolicy(
                convertType,
                bodyKey,
                securityMailPolicy,
                convertedAttachment
        );
    }
}
