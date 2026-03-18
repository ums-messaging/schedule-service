package com.ums.schedule.template.domain.email;

import com.ums.schedule.common.code.EnumMapperType;
import com.ums.schedule.common.code.EnumMapperValue;
import com.ums.schedule.template.application.response.email.EmailContentResponse;
import com.ums.schedule.template.domain.code.TemplateContentFormatEnum;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EmailContent {
    private String emailTemplateId;
    private String versionId;
    private TemplateContentFormatEnum format;
    private String content;

    public static EmailContent of(EmailContentResponse response, EnumMapperValue contentFormat) {
        EmailContent content = new EmailContent(response.emailTemplateId(), response.versionId());
        content.resolveTemplateContentFormat(contentFormat);
        content.applyTemplate(response.content());
        return content;
    }

    private EmailContent(String emailTemplateId, String versionId) {
        this.emailTemplateId = emailTemplateId;
        this.versionId = versionId;
    }

    private void resolveTemplateContentFormat(EnumMapperValue contentFormat) {
        this.format = TemplateContentFormatEnum.valueOf(contentFormat.value());
    }

    private void applyTemplate(String content) {
        this.content = content;
    }

    public boolean hasContent() {
        return StringUtils.hasText(this.content);
    }
}
