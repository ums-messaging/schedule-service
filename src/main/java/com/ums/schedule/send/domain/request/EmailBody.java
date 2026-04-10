package com.ums.schedule.send.domain.request;

import com.ums.schedule.attachment.application.model.AttachmentDto;
import com.ums.schedule.attachment.domain.SecurityPolicy;
import com.ums.schedule.common.code.EnumMapperValue;
import com.ums.schedule.template.application.response.email.EmailContentResponse;
import com.ums.schedule.template.domain.code.ConvertTypeEnum;
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
}
