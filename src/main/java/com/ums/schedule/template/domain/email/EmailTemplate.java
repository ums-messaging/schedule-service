package com.ums.schedule.template.domain.email;

import com.ums.schedule.common.code.EnumMapperValue;
import com.ums.schedule.template.application.response.TemplateResponse;
import com.ums.schedule.template.application.response.email.EmailTemplateDetailResponse;
import com.ums.schedule.template.application.response.email.EmailTemplateResponse;
import com.ums.schedule.template.domain.code.EmailTemplateSectionEnum;
import com.ums.schedule.template.domain.code.TemplateTypeEnum;
import com.ums.schedule.template.exception.TemplateContentRequiredException;
import com.ums.schedule.template.exception.TitleRequiredException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.Optional;

import static com.ums.schedule.template.domain.code.EmailTemplateSectionEnum.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EmailTemplate {
    private String templateId;
    private TemplateTypeEnum templateType;
    private String title;
    private EmailContent cover;
    private EmailContent header;
    private EmailContent body;
    private EmailContent footer;
    private String baseDir;
    private String imageDir;

    public static EmailTemplate of(EmailTemplateResponse response, EnumMapperValue templateType, Map<EmailTemplateSectionEnum, EmailContent> contentMap) {
        TemplateResponse template = response.template();
        EmailTemplateDetailResponse detail = response.emailTemplate();

        EmailTemplate ofTemplate = new EmailTemplate(template.templateId());
        ofTemplate.resolveTemplateType(templateType);
        ofTemplate.applyTemplate(contentMap);
        ofTemplate.applyTitle(template.msgTitle());
        ofTemplate.applyTemplateDir(detail.baseDir(), detail.imageDir());

        return ofTemplate;
    }

    private void applyTitle(String title) {
        this.title = title;
    }

    private void applyTemplateDir(String baseDir, String imageDir) {
        this.baseDir = baseDir;
        this.imageDir = imageDir;
    }

    private void applyTemplate(Map<EmailTemplateSectionEnum, EmailContent> contentMap) {
        this.cover = contentMap.get(COVER);
        this.header = contentMap.get(HEADER);
        this.body = contentMap.get(BODY);
        this.footer = contentMap.get(FOOTER);
    }

    private void resolveTemplateType(EnumMapperValue templateType) {
        this.templateType = TemplateTypeEnum.valueOf(templateType.value());
    }

    private EmailTemplate(String templateId) {
        this.templateId = templateId;
    }

    public void validateTitleAndBody() {
        validateTitle();
        validateBody();
    }

    private void validateBody() {
        Optional.ofNullable(body)
                .filter(b -> b.hasContent())
                .orElseThrow(TemplateContentRequiredException::ofBody);
    }

    private String validateTitle() {
        return Optional.ofNullable(this.title)
                .filter(t -> StringUtils.hasText(title))
                .orElseThrow(TitleRequiredException::of);
    }
}
