package com.ums.schedule.domain.channel.email.message;

import com.ums.schedule.domain.channel.email.security.SecurityPolicy;
import com.ums.schedule.code.email.ConvertTypeEnum;
import com.ums.schedule.code.send.ContentTypeEnum;
import com.ums.schedule.code.EnumMapperValue;
import freemarker.template.Template;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class EmailBody {
    @Enumerated(EnumType.STRING)
    @Column(name = "convert_type", nullable = false)
    private ConvertTypeEnum convertType;

    @Embedded
    private SecurityPolicy securityPolicy;

    @Transient
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
