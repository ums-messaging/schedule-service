package com.ums.schedule.template.domain.email;

import com.ums.schedule.attachment.domain.AttachmentPolicy;
import com.ums.schedule.attachment.domain.FileMetaData;
import com.ums.schedule.common.code.EnumMapperType;
import com.ums.schedule.common.code.EnumMapperValue;
import com.ums.schedule.template.application.response.email.EmailContentResponse;
import com.ums.schedule.template.domain.code.TemplateContentFormatEnum;
import com.ums.schedule.template.exception.TemplateContentRequiredException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

import java.util.List;

import static com.ums.schedule.template.domain.code.TemplateContentFormatEnum.HTML;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EmailContent {
    private TemplateContentFormatEnum format;
    private String content;

    public static EmailContent fromResponse(EmailContentResponse response) {
        EmailContent content = new EmailContent(HTML);
        return content;
    }

    public static EmailContent of(String content) {
        EmailContent emailContent = new EmailContent(TemplateContentFormatEnum.TEXT);
        emailContent.applyTemplate(content);
        return emailContent;
    }

    private EmailContent(TemplateContentFormatEnum format) {
        this.format = format;
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
