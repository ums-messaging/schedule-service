package com.ums.schedule.domain.channel.email.message;

import com.ums.schedule.domain.channel.email.security.SecurityPolicy;
import com.ums.schedule.code.email.ConvertTypeEnum;
import com.ums.schedule.code.send.ContentTypeEnum;
import com.ums.schedule.application.channel.email.dto.AttachmentDto;
import com.ums.schedule.code.EnumMapperValue;
import com.ums.schedule.adapter.api.template.email.EmailContentResponse;
import freemarker.template.Template;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EmailBody {
    private ConvertTypeEnum convertType;
    private SecurityPolicy securityPolicy;
    private Template template;

    public static EmailBody of(EnumMapperValue convertType, SecurityPolicy securityPolicy) {
        EmailBody body = new EmailBody();
        body.resolveConvertType(convertType);
        body.applySecurityPolicy(securityPolicy);
        return body;
    }

    public static EmailBody of() {
        EmailBody body = new EmailBody();
        body.resolveConvertType(EnumMapperValue.fromEnumMapperType(ConvertTypeEnum.NONE));
        return body;
    }

    private void resolveConvertType(EnumMapperValue convertType) {
        this.convertType = ConvertTypeEnum.valueOf(convertType.code());
    }
    private void applySecurityPolicy(SecurityPolicy policy) {
        this.securityPolicy = policy;
    }

    public AttachmentDto toAttachmentDto(EmailContentResponse body) {
        return this.convertType != ConvertTypeEnum.NONE ? null : AttachmentDto.of(body, convertType);
    }

    public void writeTemplate(Template template) {
        this.template = template;
    }

    public EnumMapperValue getConvertType() {
        return EnumMapperValue.fromEnumMapperType(this.convertType);
    }

    public Template getTemplate() {
        validate();
        return this.template;
    }

    public SecurityPolicy getSecurityPolicy() {
        validate();
        return this.securityPolicy;
    }

    public boolean hasSecurityPolicy() {
        return this.securityPolicy != null;
    }

    public boolean shouldConvertToPdf() {
        return this.convertType == ConvertTypeEnum.PDF;
    }

    public String getUploadFileExt() {
        return ".".concat(this.convertType.value().toLowerCase());
    }

    private void validate() {
        if(convertType == ConvertTypeEnum.NONE) {
            throw new RuntimeException();
        }
    }

    public boolean shouldConvert() {
        return this.convertType != ConvertTypeEnum.NONE ? true : false;
    }

    protected ContentTypeEnum resolveContentType() {
        switch (convertType) {
            case HTML -> {
                return ContentTypeEnum.HTML;
            }
            case PDF -> {
                return ContentTypeEnum.PDF;
            }
        }
        return ContentTypeEnum.PLAIN;
    }

}
