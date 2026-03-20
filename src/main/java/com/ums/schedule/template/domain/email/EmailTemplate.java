package com.ums.schedule.template.domain.email;

import com.ums.schedule.attachment.domain.Attachment;
import com.ums.schedule.attachment.domain.AttachmentPolicy;
import com.ums.schedule.common.code.EnumMapperValue;
import com.ums.schedule.template.application.response.TemplateResponse;
import com.ums.schedule.template.application.response.email.EmailContentResponse;
import com.ums.schedule.template.application.response.email.EmailTemplateDetailResponse;
import com.ums.schedule.template.application.response.email.EmailTemplateResponse;
import com.ums.schedule.template.domain.code.ConvertTypeEnum;
import com.ums.schedule.template.domain.code.EmailTemplateSectionEnum;
import com.ums.schedule.template.domain.code.TemplateTypeEnum;
import com.ums.schedule.template.exception.TemplateContentRequiredException;
import com.ums.schedule.template.exception.TitleRequiredException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.ums.schedule.template.domain.code.EmailTemplateSectionEnum.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EmailTemplate {
    private String templateId;
    private String title;
    private EmailContent header;
    private EmailContent body;
    private EmailContent footer;
    private String baseDir;
    private String imageDir;

    public static EmailTemplate of(String templateId, Map<EmailTemplateSectionEnum, EmailContent> contentMap) {
        EmailTemplate ofTemplate = new EmailTemplate(templateId);
        ofTemplate.applyTemplate(contentMap);
        return ofTemplate;
    }

    private void applyTemplateDir(String baseDir, String imageDir) {
        this.baseDir = baseDir;
        this.imageDir = imageDir;
    }

    private void applyTemplate(Map<EmailTemplateSectionEnum, EmailContent> contentMap) {
        this.header = contentMap.get(HEADER);
        this.body = contentMap.get(BODY);
        this.footer = contentMap.get(FOOTER);
    }
    private EmailTemplate(String templateId) {
        this.templateId = templateId;
    }

    public void validateTitleAndBody() {
        validateBody();
    }

    private void validateBody() {
        Optional.ofNullable(body)
                .filter(b -> b.hasContent())
                .orElseThrow(TemplateContentRequiredException::ofBody);
    }

}
